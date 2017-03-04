package com.se.index;

import java.io.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.google.gson.Gson;
import com.se.data.Posting;
import com.se.data.WordEntry;
import com.se.db.DatabaseUtil;

public class TokenizerMapReduce {
	public static class TokenizerMapper extends
			Mapper<Object, Text, Text, Text> {

		private static Gson gson = new Gson();

		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			Properties prop = new Properties();
			InputStream input = null;
			String PATH = "";
			try {
				input = new FileInputStream("target/conf.properties");
				prop.load(input);
				PATH = prop.getProperty("path").toString();
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
			StringTokenizer itr = new StringTokenizer(value.toString());
			if (itr.countTokens() < 2) {
				return;
			}
			String fileName = itr.nextToken();
			// String url = itr.nextToken();
			String[] parts = fileName.split("/");
			int docID = 500 * Integer.valueOf(parts[0])
					+ Integer.valueOf(parts[1]);
			File file = new File(PATH + fileName);
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
			wordEntry.setPostings(postings);
			wordEntry.setDocFrq(postings.size());
			db.insert(wordEntry);
		}
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		Properties prop = new Properties();
		InputStream input = null;
		String path = "";
		try {
			input = new FileInputStream("target/conf.properties");
			prop.load(input);
			path = prop.getProperty("path").toString();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String location = path+"bookkeeping.tsv";
		Job job = Job.getInstance(conf, "Postings Creator");
		job.setJarByClass(TokenizerMapReduce.class);
		job.setMapperClass(TokenizerMapper.class);
		job.setReducerClass(PostingsReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path(location));
		FileOutputFormat.setOutputPath(job, new Path(
				"target/output9"));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
