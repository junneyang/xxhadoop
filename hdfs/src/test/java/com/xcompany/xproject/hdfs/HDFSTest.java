package com.xcompany.xproject.hdfs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HDFSTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HDFSTest.class);
	private FileSystem fs = null;
	
	@Before
	public void setUp() throws IOException {
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://node-01:9000");
		fs = FileSystem.get(conf);
	}
	
	@After
	public void tearDown() throws IOException {
		fs.close();
	}
	
	@Test
	public void testList() throws FileNotFoundException, IOException {
		Path f = new Path("/");
		RemoteIterator<LocatedFileStatus> files = fs.listFiles(f, true);
		while (files.hasNext()) {
			LocatedFileStatus file = (LocatedFileStatus) files.next();
			LOGGER.info("====={}", file.getPath());
		}
	}
	
	@Test
	public void testPut() throws IOException {
		Path f = new Path("/put-word-count.txt");
		FSDataOutputStream fsDataOutputStream = fs.create(f, true);
		FileInputStream fileInputStream = new FileInputStream("/home/xxproject/word-count.txt");
		IOUtils.copy(fileInputStream, fsDataOutputStream);
	}
	
	@Test
	public void testGet() throws IOException {
		Path f = new Path("/put/word-count.txt");
		FSDataInputStream fsDataInputStream = fs.open(f);
		FileOutputStream fileOutputStream = new FileOutputStream("/home/xxproject/get-word-count.txt");
		IOUtils.copy(fsDataInputStream, fileOutputStream);
	}
}
