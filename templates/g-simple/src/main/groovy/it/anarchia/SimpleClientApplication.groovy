package it.anarchia

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.ServiceInstance
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.context.config.annotation.RefreshScope;
//import org.springframework.cloud.netflix.turbine.stream.EnableTurbineStream;
import org.springframework.context.annotation.ComponentScan
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard
import org.springframework.beans.factory.annotation.Value;

import com.netflix.hystrix.HystrixCommand;


@SpringBootApplication
@EnableDiscoveryClient
@RestController
//@EnableTurbineStream
@RefreshScope
@EnableCircuitBreaker
public class SimpleClientApplication {

	@Value('${anarchia.welcome}')
	String welcome
		
	@Autowired
	private DiscoveryClient discoveryClient
	
	@Autowired
	private Collaboration coll	
	
    public static void main(String[] args) {
        SpringApplication.run(SimpleClientApplication.class, args)
    }

   @RequestMapping("/welcome")
	public String elencoCandidature() {
	  List<ServiceInstance> list = discoveryClient.getInstances("nrk-project-name");
	  
	  list.each {
		  println it.uri
		  println it.instanceInfo.dump()
	  }
	  
	  return "micro is better! " + welcome
	}
	
	@RequestMapping("/localSlow")
	public String localSlow() {
		return coll.localSlow()
	}
	
	@RequestMapping("/remoteCall")
	public String remoteCall() {
		return coll.remoteCall()
	}
	
}
