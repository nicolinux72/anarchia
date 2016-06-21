package it.anarchia

import org.springframework.stereotype.Component
import org.springframework.cloud.client.ServiceInstance
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.annotation.ComponentScan
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired


import com.netflix.ribbon.proxy.annotation.Hystrix;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand

import java.util.Random

@Component
class Collaboration {

	@HystrixCommand(fallbackMethod = "defaultStores")
	public String localSlow() {
		Random rand = new Random()
		int t = rand.nextInt(12)
		Thread.sleep(t * 10)
		if (t > 10 )
		  throw new RuntimeException('oops');

		return "work!"
	}

	//@Autowired
	private RestTemplate restTemplate = new RestTemplate();

	@HystrixCommand(fallbackMethod = "defaultStores")
	public String remoteCall() {
		restTemplate.getForObject("http://nrk-project-name/welcome", String.class)
	}

	public String defaultStores() {
		return "*** fallbackMethod ***";
  }
}
