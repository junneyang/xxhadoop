package com.xcompany.xproject.mr.flowpartition;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class FlowBean implements Writable {
	
	private String phoneNum;
	private long upFlow;
	private long downFlow;
	private long sumFlow;
	
	
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public long getUpFlow() {
		return upFlow;
	}
	public void setUpFlow(long upFlow) {
		this.upFlow = upFlow;
	}
	public long getDownFlow() {
		return downFlow;
	}
	public void setDownFlow(long downFlow) {
		this.downFlow = downFlow;
	}
	public long getSumFlow() {
		return sumFlow;
	}
	public void setSumFlow(long sumFlow) {
		this.sumFlow = sumFlow;
	}
	
//	@Override
//	public String toString() {
//		return "FlowBean [phoneNum=" + phoneNum + ", upFlow=" + upFlow
//				+ ", downFlow=" + downFlow + ", sumFlow=" + sumFlow + "]";
//	}
	@Override
	public String toString() {
		return upFlow + "\t" + downFlow + "\t" + sumFlow;
	}
	
	public void write(DataOutput out) throws IOException {
		out.writeUTF(phoneNum);
		out.writeLong(upFlow);
		out.writeLong(downFlow);
		out.writeLong(sumFlow);
	}
	public void readFields(DataInput in) throws IOException {
		phoneNum = in.readUTF();
		upFlow = in.readLong();
		downFlow = in.readLong();
		sumFlow = in.readLong();
	}
	
}
