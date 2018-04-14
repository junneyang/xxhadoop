package com.xcompany.xproject.mr.flowsort;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;


public class FlowBean implements WritableComparable<FlowBean> {

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
		return phoneNum + "\t" + upFlow + "\t" + downFlow + "\t" + sumFlow;
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
	public int compareTo(FlowBean o) {
		//return 0;
		// DESC
		long thisValue = this.sumFlow;
	    long thatValue = o.getSumFlow();
	    return (thisValue < thatValue ? 1 : (thisValue == thatValue ? 0 : -1));
	}
	
}
