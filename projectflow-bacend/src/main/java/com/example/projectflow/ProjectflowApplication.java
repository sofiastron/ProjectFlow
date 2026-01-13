// package com.example.projectflow;

// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication
// public class ProjectflowApplication {

// 	public static void main(String[] args) {
// 		SpringApplication.run(ProjectflowApplication.class, args);
// 	}

// }
package com.example.projectflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectflowApplication.class, args);
        System.out.println("✅ PFE Management API démarrée sur http://localhost:8080/api");
    }
}