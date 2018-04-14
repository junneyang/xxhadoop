package com.xcompany.xproject.mr.invertedindex;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.util.StringUtils;

public class StepTwoReducer extends Reducer<Text, Text, Text, Text>{
	
	private Text result = new Text();
	
	@Override
	protected void reduce(Text key, Iterable<Text> values,
			Reducer<Text, Text, Text, Text>.Context context) throws IOException,
			InterruptedException {

		//super.reduce(arg0, arg1, arg2);
		result.set(StringUtils.join(";", values));
		context.write(key, result);
	}
}
