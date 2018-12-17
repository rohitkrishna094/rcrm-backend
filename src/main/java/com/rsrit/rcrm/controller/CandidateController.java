package com.rsrit.rcrm.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rsrit.rcrm.model.Candidate;
import com.rsrit.rcrm.model.Document;
import com.rsrit.rcrm.repository.CandidateRepository;
import com.rsrit.rcrm.util.NullAwareBeanUtilsBean;

@RestController
@RequestMapping("/candidate")
public class CandidateController {

    @Autowired
    private CandidateRepository candidateRepository;

    // Create
    @PostMapping("/save")
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
        return "";
    }

    // Delete
    @DeleteMapping("/delete/{id}")
    public void deleteCandidate(@PathVariable String id) {
        Optional<Candidate> c = this.candidateRepository.findById(id);
        if (c.isPresent())
            this.candidateRepository.delete(c.get());
    }

    // Documents endpoints for candidates
    // Create this document for this candidate with id "id"
    @PostMapping("/{id}/documents/save")
    public String documentCreate(@RequestParam("file") MultipartFile multipart, @PathVariable String id) throws IOException {
        Optional<Candidate> found = this.candidateRepository.findById(id);
        if (found.isPresent()) {
            Document d = new Document();
            d.setTitle(id + "_document");
            d.setType("resume");
            d.setFileAttachment(new Binary(BsonBinarySubType.BINARY, multipart.getBytes()));
            Candidate c = found.get();
            List<Document> docs = c.getDocuments();
            if (docs == null)
                docs = new ArrayList<>();
            docs.add(d);
            c.setDocuments(docs);
            this.candidateRepository.save(c);
            return d.toString();
        }
        return "";
    }

}
