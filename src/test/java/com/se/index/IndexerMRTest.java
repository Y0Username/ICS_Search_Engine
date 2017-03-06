package com.se.index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.se.data.Posting;
import com.se.index.IndexerMR.PostingsReducer;
import com.se.index.IndexerMR.TokenizerMapper;

public class IndexerMRTest {
	IndexerMR indexerMR = new IndexerMR();
	MapDriver<Object, Text, Text, Text> mapDriver;
	ReduceDriver<Text, Text, Text, Text> reduceDriver;
	MapReduceDriver<Object, Text, Text, Text, Text, Text> mapReduceDriver;
	Gson gson = new Gson();

	@Before
	public void setUp() {
		TokenizerMapper mapper = new TokenizerMapper();
		PostingsReducer reducer = new PostingsReducer();
		mapDriver = new MapDriver<Object, Text, Text, Text>(mapper);
		reduceDriver = ReduceDriver.newReduceDriver(reducer);
		mapReduceDriver = MapReduceDriver.newMapReduceDriver(mapper, reducer);
		Configuration conf = mapDriver.getConfiguration();
		String path = "src/test/resources/";
		conf.set("path", path);
	}

	@Test
	public void testMapper() throws IOException, InterruptedException {
		mapDriver.withInput(new Object(), new Text("46/309	www.ics.uci.edu/~magda"));
		mapDriver.withOutput(new Text("term"), new Text("posting"));
		mapDriver.runTest();
	}

	@Test
	public void testReducer() throws IOException {
		List<Text> values = new ArrayList<Text>();
		values.add(new Text(gson.toJson(new Posting(1, 1))));
		values.add(new Text(gson.toJson(new Posting(1, 1))));
		reduceDriver.withInput(new Text("6"), values);
//		reduceDriver.withOutput(null, null);
		reduceDriver.runTest();
	}

	@Test
	public void testMapReduce() throws IOException {
		mapReduceDriver.withInput(null, new Text(
				"46/309	www.ics.uci.edu/~magda"));
		List<Text> values = new ArrayList<Text>();
		values.add(new Text(gson.toJson(new Posting(1, 1))));
		values.add(new Text(gson.toJson(new Posting(1, 1))));
		mapReduceDriver.withOutput(null, null);
		mapReduceDriver.runTest();
	}

}
