package com.nic.trainingportal;


import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class TrainingPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainingPortalApplication.class, args);
		String trainingPortal = "\u001B[31m _____          _       _               ____            _        _ \n" +
                "|_   _| __ __ _(_)_ __ (_)_ __   __ _  |  _ \\ ___  _ __| |_ __ _| |\n" +
                "  | || '__/ _` | | '_ \\| | '_ \\ / _` | | |_) / _ \\| '__| __/ _` | |\n" +
                "  | || | | (_| | | | | | | | | | (_| | |  __/ (_) | |  | || (_| | |\n" +
                "  |_||_|  \\__,_|_|_| |_|_|_| |_|\\__, | |_|   \\___/|_|   \\__\\__,_|_|\n" +
                "                                |___/                            \u001B[0m";
		   System.out.println(trainingPortal);
	}

}
