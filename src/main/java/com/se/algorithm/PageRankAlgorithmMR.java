package com.se.algorithm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.se.data.PageRankData;
import com.se.db.DatabaseUtil;
import com.se.file.FileHandler;

import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;

public class PageRankAlgorithmMR {
	private static Graph<Integer, String> graph;
	private static final Double ALPHA = 0.1;
	private static PageRank<Integer, String> pageRank;
	private static final String PATH = "path";
	private static final String BOOK_KEEPING_FILE = "bookkeeping";
	private static DatabaseUtil db;
	private static Map<String, Integer> urlToDocId = new HashMap<>();

	public static class PRMapper extends Mapper<Object, Text, Text, Text> {

		public void map(Object key, Text value, Context context)
				throws InterruptedException, IOException {
			StringTokenizer itr = new StringTokenizer(value.toString());
			if (itr.countTokens() < 2) {
				return;
			}
			String filePath = itr.nextToken();
			String url = itr.nextToken();

			String[] parts = filePath.split("/");
			int docID = 500 * Integer.valueOf(parts[0])
					+ Integer.valueOf(parts[1]);
			urlToDocId.put(url, docID);
			context.write(value, value);
		}
	}

	public static class PRReducer extends Reducer<Text, Text, Text, Text> {

		public void reduce(Text term, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {

			StringTokenizer itr = new StringTokenizer(term.toString());
			if (itr.countTokens() < 2) {
				return;
			}
			String filePath = itr.nextToken();
			String url = itr.nextToken();

			String[] parts = filePath.split("/");
			int docID = 500 * Integer.valueOf(parts[0])
					+ Integer.valueOf(parts[1]);
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
					if (urlToDocId.containsKey(outgoingUrl) ) {
						int docId2 = urlToDocId.get(outgoingUrl);
						graph.addEdge(docID + "_" + docId2, docID, docId2,
								EdgeType.DIRECTED);
					}
				}
			} catch (IllegalArgumentException e) {
				System.err.println(e.getMessage());
			}

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

	public static void run() throws IOException,
			ClassNotFoundException, InterruptedException {
		graph = new DirectedSparseGraph<Integer, String>();
		db = DatabaseUtil.create();
		Configuration conf = new Configuration();
		String path = FileHandler.configFetch(PATH);
		String book = FileHandler.configFetch(BOOK_KEEPING_FILE);
		conf.set(PATH, path);
		Job job = Job.getInstance(conf, "PageRank Calculator");
		job.setJarByClass(PageRankAlgorithmMR.class);
		job.setMapperClass(PRMapper.class);
		job.setReducerClass(PRReducer.class);
		job.setOutputFormatClass(NullOutputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path(path + book));
		job.waitForCompletion(true);
		pageRank = new PageRank<>(graph, ALPHA);
		pageRank.evaluate();
		Collection<Object> docPageranks = new ArrayList<Object>();
		for (Integer vertex: graph.getVertices()) {
			PageRankData docPagerank = new PageRankData();
			docPagerank.setDocId(vertex);
			docPagerank.setScore(pageRank.getVertexScore(vertex));
			docPageranks.add(docPagerank);
		} 
		db.insert(docPageranks);
	}
}