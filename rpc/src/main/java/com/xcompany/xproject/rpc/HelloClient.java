package com.xcompany.xproject.rpc;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(HelloServer.class);
	
	@Before
	public void setUp() {
	}
	@After
	public void tearDown() {
	}
	
	@Test
	public void testHello() throws IOException {
		String bindAddress = "node-01";
		int port = 8888;
		InetSocketAddress addr = new InetSocketAddress(bindAddress, port);
		HelloProtocol proxy = RPC.getProxy(
				HelloProtocol.class, HelloProtocol.versionID, 
				addr, new Configuration());
		String resp = proxy.helloMethod("JunneYang");
		LOGGER.info(resp);
		resp = proxy.helloMethod("Ares");
		LOGGER.info(resp);
	}
}
