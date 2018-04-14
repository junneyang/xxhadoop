package com.xcompany.xproject.mr.flowsort;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

public class FlowSortReducer extends Reducer<FlowBean, NullWritable, FlowBean, NullWritable>{
	
	@Override
	protected void reduce(FlowBean key, Iterable<NullWritable> values,
			Reducer<FlowBean, NullWritable, FlowBean, NullWritable>.Context context)
			throws IOException, InterruptedException {

		//super.reduce(arg0, arg1, arg2);
		context.write(key, NullWritable.get());
	}
}
