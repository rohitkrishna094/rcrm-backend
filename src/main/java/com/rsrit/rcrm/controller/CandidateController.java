package com.rsrit.rcrm.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
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
import com.rsrit.rcrm.model.Education;
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

    /*-------------------------------Document endpoints for candidate-------------------------------*/
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

    // Delete this document if exists for this candidate id
    @DeleteMapping("/{id}/documents/delete/{docId}")
    public void deleteDocument(@PathVariable String id, @PathVariable ObjectId docId) {
        Optional<Candidate> found = this.candidateRepository.findById(id);
        if (found.isPresent()) {
            Candidate c = found.get();
            List<Document> docs = c.getDocuments();
            for (int i = 0; i < docs.size(); i++) {
                if (docs.get(i).get_id().equals(docId)) {
                    docs.remove(i);
                }
            }
            c.setDocuments(docs);
            this.candidateRepository.save(c);
        }
    }

    // Get all documents for this candidate with id id
    // May change the return type
    @GetMapping("/{id}/documents")
    public String getAllDocuments(@PathVariable String id) {
        Optional<Candidate> found = this.candidateRepository.findById(id);
        if (found.isPresent()) {
            Candidate c = found.get();
            List<Document> docs = c.getDocuments();
            if (docs == null)
                docs = new ArrayList<>();
            return docs.toString();
        }
        return "";
    }

    // Get document details by id
    // Might have to change return type later on
    @GetMapping("{id}/documents/{docId}")
    public String getDocument(@PathVariable String id, @PathVariable ObjectId docId) {
        Optional<Candidate> found = this.candidateRepository.findById(id);
        if (found.isPresent()) {
            Candidate c = found.get();
            List<Document> docs = c.getDocuments();
            if (docs == null)
                return null;
            for (int i = 0; i < docs.size(); i++) {
                if (docs.get(i).get_id().equals(docId))
                    return docs.get(i).toString();
            }
        }
        return "not found"; // handle exceptions etc
    }

    /*-------------------------------Education endpoints for candidate-------------------------------*/
    // Create endpoint for education based on candidate's id
    @PostMapping("{id}/education/add")
    public String educationCreate(@RequestBody Education edu, @PathVariable String id) {
        Optional<Candidate> found = this.candidateRepository.findById(id);
        if (found.isPresent()) {
            Candidate c = found.get();
            List<Education> edus = c.getEducations();
            if (edus == null)
                edus = new ArrayList<>();
            edus.add(edu);
            c.setEducations(edus);
            this.candidateRepository.save(c);
            List<Education> results = this.candidateRepository.findById(id).get().getEducations();
            return results.get(results.size() - 1).toString();
        }
        return "";
    }

    // Get a list of educations for this candidate with id id
    @GetMapping("/{id}/education")
    public String getEducationList(@PathVariable String id) {
        Optional<Candidate> found = this.candidateRepository.findById(id);
        if (found.isPresent()) {
            Candidate c = found.get();
            List<Education> edus = c.getEducations();
            if (edus == null)
                edus = new ArrayList<>();
            return edus.toString();
        }
        return "";
    }

    // Update education details for this candidate
    @PostMapping("{id}/education/update/{eduId}")
    public String updateEducation(@PathVariable String id, @RequestBody Education edu, @PathVariable ObjectId eduId) throws IllegalAccessException, InvocationTargetException {
        Optional<Candidate> found = this.candidateRepository.findById(id);
        if (found.isPresent()) {
            Candidate c = found.get();
            List<Education> edus = c.getEducations();
            if (edus == null) {
                return "";
            } else {
                for (int i = 0; i < edus.size(); i++) {
                    Education e = edus.get(i);
                    if (e.get_id().equals(eduId)) {
                        // this edu is the one to update
                        BeanUtilsBean notNull = new NullAwareBeanUtilsBean();
                        ObjectId oldId = e.get_id();
                        notNull.copyProperties(e, edu);
                        e.set_id(oldId);
                        break;
                    }
                }
            } // end else
            c.setEducations(edus);
            Candidate saved = this.candidateRepository.save(c);
            List<Education> results = saved.getEducations();
            for (int i = 0; i < results.size(); i++) {
                if (results.get(i).get_id().equals(eduId)) {
                    return results.get(i).toString();
                }
            }
        }
        return "";
    }

}
