package com.xcompany.xproject.mr.invertedindex;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class StepOneMapper extends Mapper<LongWritable, Text, Text, LongWritable>{
	
//	private static final Logger LOGGER = LoggerFactory.getLogger(StepOneMapper.class);
	
	private final static char SEPARATOR = '\t';
	
	private Text text = new Text();
	private static final LongWritable ONE = new LongWritable(1L);
	
	@Override
	protected void map(LongWritable key, Text value,
			Mapper<LongWritable, Text, Text, LongWritable>.Context context)
			throws IOException, InterruptedException {

		//super.map(key, value, context);
		StringTokenizer itr = new StringTokenizer(value.toString());
		while (itr.hasMoreTokens()) {
			text.set(itr.nextToken() + SEPARATOR + ((FileSplit)context.getInputSplit()).getPath().getName());
			context.write(text, ONE);	
		}
	}
}
