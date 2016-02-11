package it.anarchia.microservices

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.ServiceInstance
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.annotation.ComponentScan
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard
//import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.cloud.netflix.turbine.stream.EnableTurbineStream
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.StandardEnvironment

@Configuration
@EnableAutoConfiguration
@EnableTurbineStream
@EnableDiscoveryClient

//@EnableHystrixDashboard
public class SimpleClientApplication {
	
    
    public static void main(String[] args) {
        boolean cloudEnvironment = new StandardEnvironment().acceptsProfiles("cloud");
        new SpringApplicationBuilder(SimpleClientApplication.class).web(!cloudEnvironment).run(args);
    }



}
