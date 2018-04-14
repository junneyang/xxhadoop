package com.xcompany.xproject.hbase;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * http://blog.csdn.net/qq1010885678/article/details/51985735
*/
public class HBaseTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HBaseTest.class);
	private Admin admin = null;
	
	@Before
	public void setUp() throws IOException {
		Configuration conf = HBaseConfiguration.create();
//		conf.set("hbase.master", "node-02:16010;node-03:16010");
		conf.set("hbase.zookeeper.quorum", "node-01:2181,node-02:2181,node-03:2181");
		Connection connection = ConnectionFactory.createConnection(conf);
		admin = connection.getAdmin();
	}
	
	@After
	public void tearDown() {
	}

	public void createTable(String tableName, String... familyNames) throws IOException {
		if (admin.tableExists(TableName.valueOf(tableName))) {
			LOGGER.info("TABLE " + tableName.toString() + " ALREADY EXIST");
			return;
		}
		HTableDescriptor desc = new HTableDescriptor(TableName.valueOf(tableName));
		for (String familyName : familyNames) {
			desc.addFamily(new HColumnDescriptor(familyName));
		}
		admin.createTable(desc);
		LOGGER.info("TABLE " + tableName.toString() + " CREATED");
	}
	
	@Test
	public void testCreateTable() throws IOException {
		createTable("tbl_girls", "base_info", "extra_info");
	}
	
	@Test
	public void testPutData() throws IOException {
		Connection connection = admin.getConnection();
		Table table = connection.getTable(TableName.valueOf("tbl_girls"));
		String rowKey = "0001";
		Put put = new Put(Bytes.toBytes(rowKey));
		//put.addColumn(Bytes.toBytes(family), Bytes.toBytes(column), Bytes.toBytes(value));
		put.addColumn(Bytes.toBytes("base_info"), Bytes.toBytes("name"), Bytes.toBytes("zhangsan"));
		put.addColumn(Bytes.toBytes("base_info"), Bytes.toBytes("age"), Bytes.toBytes(28));
		put.addColumn(Bytes.toBytes("extra_info"), Bytes.toBytes("boyfriend"), Bytes.toBytes("xiaoming"));
		table.put(put);
		LOGGER.info("TABLE " + table.getName().toString() + " COLUMN ADDED");
	}
	
	@Test
	public void testPutData2() throws IOException {
		Connection connection = admin.getConnection();
		Table table = connection.getTable(TableName.valueOf("tbl_girls"));
		String rowKey = "0002";
		Put put = new Put(Bytes.toBytes(rowKey));
		//put.addColumn(Bytes.toBytes(family), Bytes.toBytes(column), Bytes.toBytes(value));
		put.addColumn(Bytes.toBytes("base_info"), Bytes.toBytes("name"), Bytes.toBytes("lisi"));
		put.addColumn(Bytes.toBytes("base_info"), Bytes.toBytes("age"), Bytes.toBytes(27));
		put.addColumn(Bytes.toBytes("extra_info"), Bytes.toBytes("boyfriend"), Bytes.toBytes("mamma"));
		table.put(put);
		LOGGER.info("TABLE " + table.getName().toString() + " COLUMN ADDED");
	}
	
	@Test
	public void testPutData3() throws IOException {
		Connection connection = admin.getConnection();
		Table table = connection.getTable(TableName.valueOf("tbl_girls"));
		String rowKey = "0003";
		Put put = new Put(Bytes.toBytes(rowKey));
		//put.addColumn(Bytes.toBytes(family), Bytes.toBytes(column), Bytes.toBytes(value));
		put.addColumn(Bytes.toBytes("base_info"), Bytes.toBytes("name"), Bytes.toBytes("wangwu"));
		put.addColumn(Bytes.toBytes("base_info"), Bytes.toBytes("age"), Bytes.toBytes(30));
		put.addColumn(Bytes.toBytes("extra_info"), Bytes.toBytes("boyfriend"), Bytes.toBytes("limei"));
		table.put(put);
		LOGGER.info("TABLE " + table.getName().toString() + " COLUMN ADDED");
	}
	
	@Test
	public void testGetData() throws IOException {
		Connection connection = admin.getConnection();
		Table table = connection.getTable(TableName.valueOf("tbl_girls"));
		String rowKey = "0001";
		Get get = new Get(Bytes.toBytes(rowKey));
		get.addColumn(Bytes.toBytes("base_info"), Bytes.toBytes("age"));
		Result result = table.get(get);
		LOGGER.info(result.toString());
		List<Cell> cells = result.listCells();
		for (Cell cell : cells) {
			int age = Bytes.toInt(cell.getQualifierArray()); 
			LOGGER.info(String.valueOf(age));
		}
		
	}
	
	@Test
	public void testScan() throws IOException {
		Connection connection = admin.getConnection();
		Table table = connection.getTable(TableName.valueOf("tbl_girls"));
		
		Scan scan = new Scan(Bytes.toBytes("0001"), Bytes.toBytes("0004"));
		// RowKeyFilter
		Filter filter = new PrefixFilter(Bytes.toBytes("000"));
		scan.setFilter(filter);
		
		Filter filter2 = new RowFilter(CompareOp.EQUAL, new SubstringComparator("000"));
		scan.setFilter(filter2);
		
		//BinaryComparator binaryComparator = new BinaryComparator(Bytes.toBytes(29));		
		Filter filter3 = new SingleColumnValueFilter(Bytes.toBytes("base_info"), Bytes.toBytes("age"), CompareOp.GREATER, Bytes.toBytes(29));
		scan.setFilter(filter3);
		
		ResultScanner resultScanner = table.getScanner(scan);
		for (Result result : resultScanner) {
			LOGGER.info(result.toString());
			int value = Bytes.toInt(result.getValue(Bytes.toBytes("base_info"), Bytes.toBytes("age")));
			LOGGER.info(String.valueOf(value));
		}
	}
	
}

