package com.xcompany.xproject.mr.wordcount;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class WordCountReducer extends Reducer<Text, LongWritable, Text, LongWritable>{

	private LongWritable result = new LongWritable();
	
	@Override
	protected void reduce(Text key, Iterable<LongWritable> values,
			Reducer<Text, LongWritable, Text, LongWritable>.Context context)
			throws IOException, InterruptedException {

		//super.reduce(arg0, arg1, arg2);
		long count = 0;
		for (LongWritable value : values) {
			count += value.get();
		}
		result.set(count);
		context.write(key, result);
	}
}
