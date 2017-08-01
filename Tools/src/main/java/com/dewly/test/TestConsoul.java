package com.dewly.test;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.NotRegisteredException;
import com.orbitz.consul.model.health.ServiceHealth;

public class TestConsoul {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Consul consul = Consul.builder().build();
		
		AgentClient agentClient = consul.agentClient();
		
		
		String serviceName = "MyService";
		String serviceId = "1";

		agentClient.register(8080, 3L, serviceName, serviceId); // registers with a TTL of 3 seconds
		try {
			agentClient.pass(serviceId);
		} catch (NotRegisteredException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HealthClient healthClient = consul.healthClient();

		List<ServiceHealth> nodes = healthClient.getHealthyServiceInstances("MyService").getResponse(); // discover only "passing" nodes
		nodes.stream().forEach(action -> {
			System.out.println(action.getNode().getAddress());
			System.out.println(new ReflectionToStringBuilder(action.getNode()));
		});
	}

}
