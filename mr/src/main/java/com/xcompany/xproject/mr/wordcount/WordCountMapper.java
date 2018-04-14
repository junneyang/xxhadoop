package com.xcompany.xproject.mr.wordcount;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/*
 * http://blog.csdn.net/boonya/article/details/54959393
 * http://blog.csdn.net/guoery/article/details/8529004
 * LongWritable: LineNumber
 * Text        : LineString
 * Text        : OutKey
 * LongWritable: OutValue
*/
public class WordCountMapper extends Mapper<LongWritable, Text, Text, LongWritable>{
	
	private final static LongWritable ONE = new LongWritable(1L);
	private Text word = new Text();
	
	@Override
	protected void map(LongWritable key, Text value,
			Mapper<LongWritable, Text, Text, LongWritable>.Context context)
			throws IOException, InterruptedException {

		//super.map(key, value, context);
		StringTokenizer itr = new StringTokenizer(value.toString());
		while (itr.hasMoreTokens()) {
			word.set(itr.nextToken());
			context.write(word, ONE);
			
		}
	}
}
