/*
 * Copyright 2016-2017 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.snowdrop.springboot.ribbon;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Say Hello Service (producer).
 *
 * @author Obsidian Quickstarts
 */
@RestController
@SpringBootApplication
public class SayHelloApplication {

	private static Logger log = LoggerFactory.getLogger(SayHelloApplication.class);

	/**
	 * String Variable HOSTNAME.
	 */
	public static final String HOSTNAME = "HOSTNAME";

	private final String hostName;

	public SayHelloApplication() {
		this.hostName = System.getenv(HOSTNAME);
	}

	@RequestMapping("/greeting")
	public String greet() {
		log.info("Access /greeting");

		List<String> greetings = Arrays.asList("Hi there", "Greetings", "Salutations");
		Random rand = new Random();

		int randomNum = rand.nextInt(greetings.size());
		return greetings.get(randomNum) + ", from " + this.hostName + " pod";
	}

	@RequestMapping("/")
	public String home() {
		log.info("Access /");
		return "Hi!" + ", from " + this.hostName + ", pod !";
	}

	public static void main(String[] args) {
		SpringApplication.run(SayHelloApplication.class, args);
	}
}
