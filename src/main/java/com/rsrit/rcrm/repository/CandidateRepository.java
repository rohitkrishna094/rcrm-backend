package com.rsrit.rcrm.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rsrit.rcrm.model.Candidate;

public interface CandidateRepository extends MongoRepository<Candidate, String> {
    public Candidate findByFirstName(String firstName);

    public List<Candidate> findByLastName(String lastName);
}
