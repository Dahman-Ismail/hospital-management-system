package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(UserRepository userRepository) {
        System.out.println("-------------------------------------login");
        return args -> {
            if (userRepository.findByEmail("admin@admin.com") == null) {
                System.out.println("---------test----------------login");
                userRepository.save(new User("admin@admin.com", "password", "ADMIN"));
            }
        };
    }
}
