package com.rsrit.rcrm.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.tika.mime.MimeTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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

    @Autowired
    private MongoTemplate mongoTemplate;

    // @GetMapping("/search")
    // public String search(@RequestParam(name = "query", defaultValue = "") String query, @RequestParam(value = "page", defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int
    // size) {
    // // String newQueryString = "\"{$where: \"JSON.stringify(this).indexOf(?)!=-1\"}\"";
    // // newQueryString = newQueryString.replace("?", query);
    // // System.out.println(newQueryString);
    // // Page<Candidate> pages = candidateRepository.findBySearch(newQueryString, PageRequest.of(pageNumber, size));
    // List<Candidate> pages = candidateRepository.findBySearch(query);
    // return pages.toString();
    // // return "";
    // }

    @GetMapping("/search")
    public String search(@RequestParam(name = "query", defaultValue = "") String searchString, @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int size) {
        String s = "{$where:\"JSON.stringify(this).indexOf('?')!=-1\"}";
        s = s.replace("?", searchString);
        Query q = new BasicQuery(s);

        List<Candidate> candidates = mongoTemplate.find(q, Candidate.class);
        return String.valueOf(candidates.size());
        // return candidates.toString();
    }

    // Create
    @PostMapping("/save")
    public String create(@RequestBody Candidate c) {
        Candidate saved = this.candidateRepository.save(c);
        return saved.toString();
        // TODO : Add exception handling -> unique constraint on username or something so we return an error saying candidate with name username already exists or something
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
                e.printStackTrace();
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "RCRM-Error: Error while initializing model class from input");
            }
            this.candidateRepository.save(found.get());
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "RCRM-Error: Requested candidate not found");
        // return "";
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
    public String documentCreate(@RequestParam("file") MultipartFile multipart, @PathVariable String id) {
        String fileExtension;
        try {
            fileExtension = FileStorageService.getFileExtensionFromMimeType(multipart.getContentType());
        } catch (MimeTypeException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "RCRM-Error: Input Mime Type not recognized. Please give a proper valid file extension.");
        }

        Optional<Candidate> found = this.candidateRepository.findById(id);
        if (found.isPresent()) {
            Document d = new Document();
            String fileName = id + "_doc_" + d.get_id();
            d.setTitle(fileName);
            d.setType(fileExtension);
            String url = "";
            try {
                url = fileStorageService.saveToAws(multipart, fileName);
            } catch (MimeTypeException | IOException e) {
                e.printStackTrace();
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "RCRM-Error: Unable to upload the file to our server.");
            }
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
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "RCRM-Error: Requested candidate not found");
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
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "RCRM-Error: Requested candidate not found");
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
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "RCRM-Error: Requested candidate not found");
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
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "RCRM-Error: Requested candidate not found");
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
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "RCRM-Error: Requested candidate not found");
    }

    // Update education details for this candidate
    @PostMapping("{id}/education/update/{eduId}")
    public String updateEducation(@PathVariable String id, @RequestBody Education edu, @PathVariable String eduId) {
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
                        try {
                            notNull.copyProperties(e, edu);
                        } catch (IllegalAccessException | InvocationTargetException e1) {
                            e1.printStackTrace();
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "RCRM-Error: Error while initializing model class from input");
                        }
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
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "RCRM-Error: Requested candidate not found");
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
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "RCRM-Error: Requested candidate not found");
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
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "RCRM-Error: Requested candidate not found");
    }

    // Update workExp details for this candidate id
    @PostMapping("{id}/experience/update/{expId}")
    public String updateWorkExp(@PathVariable String id, @PathVariable String expId, @RequestBody WorkExperience exp) {
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
                        try {
                            notNull.copyProperties(w, exp);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "RCRM-Error: Error while initializing model class from input");
                        }
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
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "RCRM-Error: Requested candidate not found");
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
