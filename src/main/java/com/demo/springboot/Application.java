package com.demo.springboot;

import com.demo.springboot.core.threading.AsyncTask;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		try {
			SpringApplication.run(Application.class, args);
		} finally {
			// releases all the resources associated with the async task execution runtime...
			AsyncTask.dispose();
		}
	}

}
