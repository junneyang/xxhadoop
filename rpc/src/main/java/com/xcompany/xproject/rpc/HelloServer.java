package com.xcompany.xproject.rpc;

import java.io.IOException;

import org.apache.hadoop.HadoopIllegalArgumentException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.RPC.Builder;
import org.apache.hadoop.ipc.RPC.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloServer implements HelloProtocol {

	private static final Logger LOGGER = LoggerFactory.getLogger(HelloServer.class);
	
	public String helloMethod(String name) {
		LOGGER.info(name);
		return "Hello " + name;
	}
	
	public static void main(String[] args) throws HadoopIllegalArgumentException, IOException {
		Configuration conf = new Configuration();
		Builder builder = new RPC.Builder(conf);
		String bindAddress = "node-01";
		int port = 8888;
		builder.setBindAddress(bindAddress)
			.setPort(8888)
			.setProtocol(HelloProtocol.class)
			.setInstance(new HelloServer());
		Server server = builder.build();
		LOGGER.info("Server start to listen on " + port);
		server.start();
	}

}
