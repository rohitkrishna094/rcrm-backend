package com.rsrit.rcrm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.rsrit.rcrm.model.ElasticCandidate;

public interface SearchRepository extends ElasticsearchRepository<ElasticCandidate, String> {
    Page<ElasticCandidate> findByFirstName(String name, Pageable pageable);
}
