package com.xcompany.xproject.mr.flowsort;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlowSortMapper extends Mapper<LongWritable, Text, FlowBean, NullWritable> {

	private static final Logger LOGGER = LoggerFactory.getLogger(FlowSortMapper.class);
	
	private FlowBean flowBean = new FlowBean();
	
	private String line = null;
	private final static char SEPARATOR = '\t';
	
	private String phoneNum = null;
	private long upFlow = 0;
	private long downFlow = 0;
	private long sumFlow = 0;
	
	@Override
	protected void map(LongWritable key, Text value,
			Mapper<LongWritable, Text, FlowBean, NullWritable>.Context context)
			throws IOException, InterruptedException {

		//super.map(key, value, context);
		line = value.toString();
		String[] fields = StringUtils.split(line, SEPARATOR);
		if (fields.length != 4) {
			LOGGER.error("invalid line: {}", line);
			System.err.println("invalid line: " + line);
		} else {
			phoneNum = fields[0];
			upFlow = Long.parseLong(fields[1]);
			downFlow = Long.parseLong(fields[2]);
			sumFlow = Long.parseLong(fields[3]);
			flowBean.setPhoneNum(phoneNum);
			flowBean.setUpFlow(upFlow);
			flowBean.setDownFlow(downFlow);
			flowBean.setSumFlow(sumFlow);
			context.write(flowBean, NullWritable.get());
		}
	}
}
