package com.xcompany.xproject.mr.flowpartition;

import java.util.HashMap;

import org.apache.hadoop.mapreduce.Partitioner;


public class FlowPartition<K, V> extends Partitioner<K, V>{

	// Load Once, Speed Up
	private static HashMap<String, Integer> partitionMap = new HashMap<String, Integer>();
	private static void loadData() {
		partitionMap.put("135", 0);
		partitionMap.put("136", 1);
		partitionMap.put("137", 2);
		partitionMap.put("138", 3);
	}
	static {
		loadData();
	}
	
	@Override
	public int getPartition(K key, V value, int numPartitions) {
		//return 0;
		String preKey = key.toString().substring(0,3);
		return (partitionMap.get(preKey) == null) ? 4 : partitionMap.get(preKey);
	}
}

