package com.simplilearn.ecomorg.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simplilearn.ecomorg.model.S3Documents;

public interface S3DocumentRepository extends JpaRepository<S3Documents, Integer>{

}
