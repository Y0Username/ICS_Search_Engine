package com.se.algorithm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.se.data.AnchorTextToken;
import com.se.db.DatabaseUtil;
import com.se.file.FileHandler;
import com.se.index.WordsTokenizer;

public class AnchorTextProcessingMR {
	private static final String PATH = "path";
	private static final String BOOK_KEEPING_FILE = "bookkeeping";
	private static DatabaseUtil db;
	private static Map<String, Integer> urlToDocId;

	public static class AnchorTextMapper extends
			Mapper<Object, Text, Text, IntWritable> {

		public void map(Object key, Text value, Context context)
				throws InterruptedException, IOException {
			StringTokenizer itr = new StringTokenizer(value.toString());
			if (itr.countTokens() < 2) {
				return;
			}
			String filePath = itr.nextToken();
			String url = itr.nextToken();

			int sourceDocId = getDocumentId(filePath);
			Configuration conf = context.getConfiguration();
			String path = conf.get(PATH);
			File file = new File(path + filePath);
			Document document = null;
			try {
				document = Jsoup.parse(file, "UTF-8", url);
				for (Element element : document.select("a[href]")) {
					String outgoingUrl = element.absUrl("href");
					if (outgoingUrl == null || outgoingUrl.isEmpty()) {
						continue;
					}
					outgoingUrl = trim(outgoingUrl);
					Integer targetDocId = urlToDocId.get(outgoingUrl);
					if (targetDocId == null) {
						continue;
					}
					List<String> tokens = WordsTokenizer.tokenize(element
							.text());
					for (String token : tokens) {
						context.write(new Text(token), new IntWritable(
								targetDocId));
					}
				}
			} catch (IllegalArgumentException e) {
				System.err.println(e.getMessage());
			}

		}

		private int getDocumentId(String filePath) {
			String[] parts = filePath.split("/");
			return 500 * Integer.valueOf(parts[0]) + Integer.valueOf(parts[1]);
		}

		private String trim(String outgoingUrl) {
			outgoingUrl = outgoingUrl.replaceFirst("^(http://|https://)", "");
			if (outgoingUrl.endsWith("/")) {
				outgoingUrl = outgoingUrl
						.substring(0, outgoingUrl.length() - 1);
			}
			return outgoingUrl;
		}
	}

	public static class AnchorTextReducer extends
			Reducer<Text, IntWritable, Text, Text> {

		public void reduce(Text term, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException {
			List<Integer> targetDocumentIds = new ArrayList<Integer>();
			for (IntWritable targetDocumentId : values) {
				targetDocumentIds.add(targetDocumentId.get());
			}
			Collections.sort(targetDocumentIds);
			AnchorTextToken anchorTextToken = new AnchorTextToken();
			anchorTextToken.setTargetDocIds(targetDocumentIds);
			anchorTextToken.setTerm(term.toString());
			db.insert(anchorTextToken);
		}
	}

	public static void run(Map<String, Integer> urlToDocIdMap) throws IOException, ClassNotFoundException,
			InterruptedException {
		urlToDocId = urlToDocIdMap;
		db = DatabaseUtil.create();
		Configuration conf = new Configuration();
		String path = FileHandler.configFetch(PATH);
		String book = FileHandler.configFetch(BOOK_KEEPING_FILE);
		conf.set(PATH, path);
		Job job = Job.getInstance(conf, "Anchor Text Collection Creator");
		job.setJarByClass(PageRankAlgorithmMR.class);
		job.setMapperClass(AnchorTextMapper.class);
		job.setReducerClass(AnchorTextReducer.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setOutputFormatClass(NullOutputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path(path + book));
		job.waitForCompletion(true);
		db.close();
	}

}