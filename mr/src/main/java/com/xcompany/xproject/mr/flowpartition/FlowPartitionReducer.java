package com.xcompany.xproject.mr.flowpartition;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class FlowPartitionReducer extends Reducer<Text, FlowBean, Text, FlowBean>{
	
	private FlowBean result = new FlowBean();
	
	@Override
	protected void reduce(Text key, Iterable<FlowBean> values,
			Reducer<Text, FlowBean, Text, FlowBean>.Context context)
			throws IOException, InterruptedException {

		//super.reduce(arg0, arg1, arg2);
		long upFlow = 0;
		long downFlow = 0;
		//long flowSum = 0;
		for (FlowBean flowBean : values) {
			upFlow += flowBean.getUpFlow();
			downFlow += flowBean.getDownFlow();
			//flowSum += flowBean.getSumFlow();
		}
		result.setPhoneNum(key.toString());
		result.setUpFlow(upFlow);
		result.setDownFlow(downFlow);
		//result.setSumFlow(flowSum);
		result.setSumFlow(upFlow + downFlow);
		context.write(key, result);
	}
}
