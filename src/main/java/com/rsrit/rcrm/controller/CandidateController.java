package com.rsrit.rcrm.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.tika.mime.MimeTypeException;
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
import com.rsrit.rcrm.model.WorkExperience;
import com.rsrit.rcrm.repository.CandidateRepository;
import com.rsrit.rcrm.service.FileStorageService;
import com.rsrit.rcrm.util.NullAwareBeanUtilsBean;

@RestController
@RequestMapping("/candidate")
public class CandidateController {

    @Autowired
    private FileStorageService fileStorageService;

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
    public String documentCreate(@RequestParam("file") MultipartFile multipart, @PathVariable String id) throws IOException, MimeTypeException {
        String fileExtension = FileStorageService.getFileExtensionFromMimeType(multipart.getContentType());
        Optional<Candidate> found = this.candidateRepository.findById(id);
        if (found.isPresent()) {
            Document d = new Document();
            String fileName = id + "_doc_" + d.get_id();
            d.setTitle(fileName);
            d.setType(fileExtension);
            String url = fileStorageService.saveToAws(multipart, fileName);
            d.setUrl(url);
            d.setTitle(fileName);
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
    @DeleteMapping("/{id}/documents/{docId}")
    public void deleteDocument(@PathVariable String id, @PathVariable String docId) {
        Optional<Candidate> found = this.candidateRepository.findById(id);
        if (found.isPresent()) {
            Candidate c = found.get();
            List<Document> docs = c.getDocuments();
            if (docs != null) {
                for (int i = 0; i < docs.size(); i++) {
                    if (docs.get(i).get_id().equals(docId)) {
                        fileStorageService.deleteFromAws(docs.get(i).getUrl());
                        docs.remove(i);
                        break;

                    }
                }
                c.setDocuments(docs);
                this.candidateRepository.save(c);
            }
        }
    }

    // Delete all docs for this candidate
    @DeleteMapping("/{id}/documents/")
    public void deleteAllDocuments(@PathVariable String id) {
        System.out.println("hello");
        Optional<Candidate> found = this.candidateRepository.findById(id);
        if (found.isPresent()) {
            Candidate c = found.get();
            List<Document> docs = c.getDocuments();
            if (docs != null) {
                for (int i = 0; i < docs.size(); i++) {
                    fileStorageService.deleteFromAws(docs.get(i).getUrl());
                }
                docs = null;
                c.setDocuments(docs);
                this.candidateRepository.save(c);
            }
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
    public String getDocument(@PathVariable String id, @PathVariable String docId) {
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
            List<Education> results = this.candidateRepository.save(c).getEducations();
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
    public String updateEducation(@PathVariable String id, @RequestBody Education edu, @PathVariable String eduId) throws IllegalAccessException, InvocationTargetException {
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
                        String oldId = e.get_id();
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

    // Delete education for this candidate id
    @DeleteMapping("{id}/education/delete/{eduId}")
    public void deleteEducation(@PathVariable String id, @PathVariable String eduId) {
        Optional<Candidate> found = this.candidateRepository.findById(id);
        if (found.isPresent()) {
            Candidate c = found.get();
            List<Education> edus = c.getEducations();
            if (edus != null) {
                for (int i = 0; i < edus.size(); i++) {
                    Education e = edus.get(i);
                    if (e.get_id().equals(eduId)) {
                        edus.remove(i);
                        break;
                    }
                } // end for
                c.setEducations(edus);
                this.candidateRepository.save(c);
            }
        }
    }

    /*-------------------------------WorkExperience endpoints for candidate-------------------------------*/
    // Create workexp for this candidate id
    @PostMapping("{id}/experience/add")
    public String workExpCreate(@RequestBody WorkExperience we, @PathVariable String id) {
        Optional<Candidate> found = this.candidateRepository.findById(id);
        if (found.isPresent()) {
            Candidate c = found.get();
            List<WorkExperience> wes = c.getWorkExperienceList();
            if (wes == null)
                wes = new ArrayList<>();
            wes.add(we);
            c.setWorkExperienceList(wes);
            List<WorkExperience> results = this.candidateRepository.save(c).getWorkExperienceList();
            return results.get(results.size() - 1).toString();
        }
        return "";
    }

    // Get a list of work experiences for this candidate id
    @GetMapping("/{id}/experience")
    public String getWorkExpList(@PathVariable String id) {
        Optional<Candidate> found = this.candidateRepository.findById(id);
        if (found.isPresent()) {
            Candidate c = found.get();
            List<WorkExperience> exps = c.getWorkExperienceList();
            if (exps == null)
                exps = new ArrayList<>();
            return exps.toString();
        }
        return "";
    }

    // Update workExp details for this candidate id
    @PostMapping("{id}/experience/update/{expId}")
    public String updateWorkExp(@PathVariable String id, @PathVariable String expId, @RequestBody WorkExperience exp) throws IllegalAccessException, InvocationTargetException {
        Optional<Candidate> found = this.candidateRepository.findById(id);
        if (found.isPresent()) {
            Candidate c = found.get();
            List<WorkExperience> works = c.getWorkExperienceList();
            if (works == null)
                return "";
            else {
                for (int i = 0; i < works.size(); i++) {
                    WorkExperience w = works.get(i);
                    if (w.get_id().equals(expId)) {
                        // this work exp is the one to update
                        BeanUtilsBean notNull = new NullAwareBeanUtilsBean();
                        String oldId = w.get_id();
                        notNull.copyProperties(w, exp);
                        w.set_id(oldId);
                        break;
                    }
                }
            } // end else
            c.setWorkExperienceList(works);
            Candidate saved = this.candidateRepository.save(c);
            List<WorkExperience> results = saved.getWorkExperienceList();
            for (int i = 0; i < results.size(); i++) {
                if (results.get(i).get_id().equals(expId))
                    return results.get(i).toString();
            }
        }
        return "";
    }

    // Delete workExp details for this candidate id
    @DeleteMapping("{id}/experience/delete/{expId}")
    public void deleteWorkExp(@PathVariable String id, @PathVariable String expId) {
        Optional<Candidate> found = this.candidateRepository.findById(id);
        if (found.isPresent()) {
            Candidate c = found.get();
            List<WorkExperience> works = c.getWorkExperienceList();
            if (works != null) {
                for (int i = 0; i < works.size(); i++) {
                    WorkExperience w = works.get(i);
                    if (w.get_id().equals(expId)) {
                        works.remove(i);
                        break;
                    }
                } // end for
                c.setWorkExperienceList(works);
                this.candidateRepository.save(c);
            }
        }
    }
}
