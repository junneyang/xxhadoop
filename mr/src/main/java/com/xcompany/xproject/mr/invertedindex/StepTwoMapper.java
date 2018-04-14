package com.xcompany.xproject.mr.invertedindex;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.StringUtils;

public class StepTwoMapper extends Mapper<LongWritable, Text, Text, Text> {
	
	private Text textKey = new Text();
	private Text textValue = new Text();
	
	private final static char SEPARATOR = '\t';
	private String line = null;
	private String word = null;
	private String fileName = null;
	private String count = null;
	
	@Override
	protected void map(LongWritable key, Text value,
			Mapper<LongWritable, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {

		//super.map(key, value, context);
		line = value.toString();
		String[] splits = StringUtils.split(line, SEPARATOR);
		word = splits[0];
		fileName = splits[1];
		count = splits[2];
		textKey.set(word);
		textValue.set(fileName + SEPARATOR + count);
		context.write(textKey, textValue);
	}
}
