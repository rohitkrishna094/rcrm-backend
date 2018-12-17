package com.rsrit.rcrm.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rsrit.rcrm.model.Candidate;
import com.rsrit.rcrm.repository.CandidateRepository;
import com.rsrit.rcrm.util.NullAwareBeanUtilsBean;

@RestController
@RequestMapping("/candidate")
public class CandidateController {

    @Autowired
    private CandidateRepository candidateRepository;

    // Create
    @PostMapping("/create")
    public String create(@RequestBody Candidate c) {
        Candidate saved = this.candidateRepository.save(c);
        return saved.toString();
    }

    // Read
    @GetMapping("/all")
    public String getAllCandidates() {
        return this.candidateRepository.findAll().toString();
    }

    // Update
    @PostMapping("/details/update/{id}")
    public String updateCandidateDetails(@RequestBody Candidate c, @PathVariable String id) {
        Optional<Candidate> found = this.candidateRepository.findById(id);
        if (found.isPresent()) {
            BeanUtilsBean notNull = new NullAwareBeanUtilsBean();
            try {
                notNull.copyProperties(found.get(), c);
            } catch (IllegalAccessException | InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            this.candidateRepository.save(found.get());
        }
        // System.out.println(found.isPresent());
        return "";
    }

    // Delete
    @DeleteMapping("/delete/{id}")
    public void deleteCandidate(@PathVariable String id) {
        Optional<Candidate> c = this.candidateRepository.findById(id);
        if (c.isPresent())
            this.candidateRepository.delete(c.get());
    }

}
