package com.se.index;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;

import com.google.gson.Gson;
import com.se.data.Posting;
import com.se.data.WordEntry;
import com.se.db.DatabaseUtil;
import com.se.file.FileHandler;

public class IndexerMR {
	private static final String PATH = "path";

	public static class TokenizerMapper extends
			Mapper<Object, Text, Text, Text> {

		private static Gson gson = new Gson();

		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			Configuration conf = context.getConfiguration();
			String path = conf.get(PATH);
			StringTokenizer itr = new StringTokenizer(value.toString());
			if (itr.countTokens() < 2) {
				return;
			}
			String filePath = itr.nextToken();
			// String url = itr.nextToken();
			String[] parts = filePath.split("/");
			int docID = 500 * Integer.valueOf(parts[0])
					+ Integer.valueOf(parts[1]);
			File file = new File(path + filePath);

			Map<String, Posting> postingsMap = Tokenizer.tokenize(file, docID);

			for (Entry<String, Posting> entry : postingsMap.entrySet()) {
				context.write(new Text(entry.getKey()),
						new Text(gson.toJson(entry.getValue())));
			}
		}
	}

	public static class PostingsReducer extends Reducer<Text, Text, Text, Text> {
		private static DatabaseUtil db = new DatabaseUtil();
		private static Gson gson = new Gson();

		public void reduce(Text term, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			WordEntry wordEntry = new WordEntry();
			wordEntry.setTerm(term.toString());
			List<Posting> postings = new ArrayList<Posting>();
			for (Text value : values) {
				postings.add(gson.fromJson(value.toString(), Posting.class));
			}
			Collections.sort(postings);
			wordEntry.setPostings(postings);
			wordEntry.setDocFrq(postings.size());
			db.insert(wordEntry);
		}
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		String path = FileHandler.configFetch(PATH);
		conf.set(PATH, path);
		String fileName = "bookkeeping.tsv";
		Job job = Job.getInstance(conf, "Postings Creator");
		job.setJarByClass(IndexerMR.class);
		job.setMapperClass(TokenizerMapper.class);
		job.setReducerClass(PostingsReducer.class);
		job.setOutputFormatClass(NullOutputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path(path + fileName));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}