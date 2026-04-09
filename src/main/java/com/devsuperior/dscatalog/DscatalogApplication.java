package com.devsuperior.dscatalog;

import com.devsuperior.dscatalog.services.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DscatalogApplication implements CommandLineRunner{

	private static Logger LOG = LoggerFactory.getLogger(S3Service.class);

	public static void main(String[] args) {
		SpringApplication.run(DscatalogApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		LOG.info("Start");
	}
}
