package org.obsidiantoaster.quickstart.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

@RestController
public class Controller
{
	@RequestMapping("/")
	public String health()
	{
		//For Ribbon pings
		return "";
	}

	@RequestMapping("/test")
	public String test(@RequestParam(value="indent", defaultValue="--") String indent)
	{
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append( indent ).append( " backend service called at " ).append( LocalTime.now() ).append( "<br/>" );
		delay();
		stringBuilder.append( indent ).append( " backend service finished at " ).append( LocalTime.now() ).append( "<br/>" );
		return stringBuilder.toString();
	}

	private static synchronized void delay()
	{
		try
		{
			Thread.sleep( 1000 );
		}
		catch( InterruptedException e )
		{
			e.printStackTrace();
		}
	}
}