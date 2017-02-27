package org.obsidiantoaster.quickstart.service;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixThreadPoolKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;

@RestController
@RibbonClient( name = "backend" )
public class Controller {
	@LoadBalanced
	@Bean
	RestTemplate restTemplate()
	{
		return new RestTemplate();
	}

	@Autowired
	RestTemplate restTemplate;

	@RequestMapping( "/test" )
	public String test(@RequestParam( value = "count", defaultValue = "1" ) int count)
	{
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append( "-- presentation service called at " ).append( LocalTime.now() ).append( "<br/>" );
		List<Observable<String>> observables = new ArrayList<>();
		for( int index = 0; index < count; index++ )
		{
			observables.add( new BackendCall().toObservable() );
		}
		Observable<String> zipped = Observable.zip( observables, objects->
		{
			StringBuilder responses = new StringBuilder();
			for( Object object : objects )
			{
				responses.append( object );
			}
			return responses.toString();
		} );
		stringBuilder.append( zipped.toBlocking().first() );
		stringBuilder.append( "-- presentation service finished at " ).append( LocalTime.now() ).append( "<br/>" );
		return stringBuilder.toString();
	}

	public class BackendCall extends HystrixCommand<String>
	{
		BackendCall()
		{
			super( HystrixCommandGroupKey.Factory.asKey( "BackendCall" ), HystrixThreadPoolKey.Factory.asKey( "BackendCallThread" ) );
		}

		@Override
		protected String run() throws Exception
		{
			return restTemplate.getForObject( "http://backend/test?indent={indent}", String.class, "----" );
		}

		@Override
		protected String getFallback()
		{
			return "FAILED <br/>";
		}
	}
}