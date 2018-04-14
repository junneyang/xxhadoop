package com.xcompany.xproject.mr.wordcount;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * conf: copy hadoop conf to src/main/resources dir or exe jar on hadoop node
 * export: wordcount.jar
 * example: hadoop jar wordcount.jar com.xcompany.xproject.mr.wordcount.WordCountJob /word-count/input /word-count/output
*/
public class WordCountJob {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(WordCountJob.class);

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {
		
		Configuration conf = new Configuration();
		//conf.set("fs.defaultFS", "hdfs://node-01:9000");
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();

		String commaSeparatedPaths = null;
		String outputDir = null;
		if (otherArgs.length == 2) {
			commaSeparatedPaths = otherArgs[0];
			outputDir = otherArgs[1];
		} else {
			System.err.println("Usage: <in>[,<in>...] <out>");
			System.exit(-1);
		}

		LOGGER.info("==========job start");
		Job job = Job.getInstance(conf);
		job.setJobName("WordCountJob");
		job.setJarByClass(WordCountJob.class);
		
		job.setMapperClass(WordCountMapper.class);
		job.setCombinerClass(WordCountReducer.class);
		job.setReducerClass(WordCountReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(LongWritable.class);

		FileInputFormat.setInputPaths(job, commaSeparatedPaths);
		FileOutputFormat.setOutputPath(job, new Path(outputDir));

		if (job.waitForCompletion(true)) {
			LOGGER.info("==========job success");
		} else {
			LOGGER.info("==========job failed");
		}
	}
}
