package com.xcompany.xproject.mr.flowsum;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlowSumMapper extends Mapper<LongWritable, Text, Text, FlowBean> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FlowSumMapper.class);
	
	private String line = null;
	private final static char SEPARATOR = '\t';
	
	private String phoneNum = null;
	private long upFlow = 0;
	private long downFlow = 0;
	//private long sumFlow = 0;
	
	private Text text = new Text();
	private FlowBean flowBean = new FlowBean();
	
	@Override
	protected void map(LongWritable key, Text value,
			Mapper<LongWritable, Text, Text, FlowBean>.Context context)
			throws IOException, InterruptedException {

		//super.map(key, value, context);
		line = value.toString();
		String[] fields = StringUtils.split(line, SEPARATOR);
		if (fields.length != 11) {
			LOGGER.error("invalid line: {}", line);
			System.err.println("invalid line: " + line);
		} else {
			phoneNum = fields[1];
			upFlow = Long.parseLong(fields[8]);
			downFlow = Long.parseLong(fields[9]);
			flowBean.setPhoneNum(phoneNum);
			flowBean.setUpFlow(upFlow);
			flowBean.setDownFlow(downFlow);
			//sumFlow = upFlow + downFlow;
			flowBean.setSumFlow(upFlow + downFlow);
			text.set(phoneNum);
			context.write(text, flowBean);
		}
		
	}
}
