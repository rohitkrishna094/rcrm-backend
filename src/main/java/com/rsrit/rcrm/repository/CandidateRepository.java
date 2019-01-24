package com.rsrit.rcrm.repository;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.rsrit.rcrm.model.Candidate;

public interface CandidateRepository extends MongoRepository<Candidate, String> {

    public Candidate findByFirstName(String firstName);

    public List<Candidate> findByLastName(String lastName);

    public Optional<Candidate> findById(ObjectId id);

    // @Query("{$text: {$search: ?0}}")
    // @Query("{$where: \"JSON.stringify(this).indexOf('gmail')!=-1\"}")
    @Query("{$where: ?0}")
    public List<Candidate> findBySearch(String query);
}
