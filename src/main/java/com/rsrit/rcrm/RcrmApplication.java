package com.rsrit.rcrm;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.github.javafaker.Faker;
import com.rsrit.rcrm.model.Candidate;
import com.rsrit.rcrm.repository.CandidateRepository;

@SpringBootApplication
@EnableMongoRepositories
public class RcrmApplication implements CommandLineRunner {

    @Autowired
    private CandidateRepository candidateRepository;

    Random rand = new Random();

    public static void main(String[] args) {
        SpringApplication.run(RcrmApplication.class, args);
    }

    @Override
    public void run(String... args) {
        candidateRepository.deleteAll();

        // save n random entities
        this.save(20);
    }

    // Test utility function to save n candidate entities
    public void save(int n) {
        Faker faker = new Faker();
        for (int i = 0; i < n; i++) {
            String name = faker.name().fullName();
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String[] emailDomains = { "gmail", "yahoo", "ymail", "live", "hotmail" };
            String emailAddress = firstName + "." + lastName + "@" + emailDomains[rand.nextInt(emailDomains.length)] + ".com";
            Candidate c = new Candidate(firstName, lastName, emailAddress);
            candidateRepository.save(c);
        }
    }

}
