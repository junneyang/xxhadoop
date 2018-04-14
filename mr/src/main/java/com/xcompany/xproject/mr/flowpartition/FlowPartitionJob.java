package com.xcompany.xproject.mr.flowpartition;

import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlowPartitionJob extends Configured implements Tool {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FlowPartitionJob.class);

	public static void main(String[] args) throws Exception {
		
		Date startTime = new Date();
		LOGGER.info("==========job started: " + startTime);
		int res = ToolRunner.run(new Configuration(), new FlowPartitionJob(), args);
		Date endTime = new Date();
		LOGGER.info("==========job ended: " + endTime);
		LOGGER.info("==========job took: " + (endTime.getTime() - startTime.getTime())/1000 + " seconds");
		System.exit(res);
	}

	public int run(String[] args) throws Exception {
		
	    /*Configuration conf = getConf();
	    JobClient client = new JobClient(conf);
	    ClusterStatus cluster = client.getClusterStatus();
	    int num_reduces = (int) (cluster.getMaxReduceTasks() * 0.9);
	    String join_reduces = conf.get(REDUCES_PER_HOST);
	    if (join_reduces != null) {
	       num_reduces = cluster.getTaskTrackers() *
	                       Integer.parseInt(join_reduces);
	    }
	    // Set user-supplied (possibly default) job configs
	    job.setNumReduceTasks(num_reduces);*/
	    
	    
		Configuration conf = new Configuration();
		//conf.set("fs.defaultFS", "hdfs://node-01:9000");
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();

		String commaSeparatedPaths = null;
		String outputDir = null;
		if (otherArgs.length == 2) {
			commaSeparatedPaths = otherArgs[0];
			outputDir = otherArgs[1];
		} else {
			System.err.println("Usage: <in>[,<in>...] <out>");
			//System.exit(-1);
			return -1;
		}
		

		Job job = Job.getInstance(conf);
		job.setJobName("FlowPartitionJob");
		job.setJarByClass(FlowPartitionJob.class);
		
//		job.setInputFormatClass(TextInputFormat.class);
//		job.setOutputFormatClass(TextOutputFormat.class);
		
		job.setMapperClass(FlowPartitionMapper.class);
		//job.setCombinerClass(WordCountReducer.class);
		job.setReducerClass(FlowPartitionReducer.class);
		
		job.setPartitionerClass(FlowPartition.class);
		job.setNumReduceTasks(5);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(FlowBean.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(FlowBean.class);

		FileInputFormat.setInputPaths(job, commaSeparatedPaths);
		FileOutputFormat.setOutputPath(job, new Path(outputDir));

		return job.waitForCompletion(true) ? 0 : 1;
	}
}
