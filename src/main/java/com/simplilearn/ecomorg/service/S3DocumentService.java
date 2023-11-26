package com.simplilearn.ecomorg.service;

import com.amazonaws.services.s3.model.S3Object;
import com.simplilearn.ecomorg.model.S3Documents;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface S3DocumentService {

	public void upload(MultipartFile file) throws IOException;
    public S3Object download(int id);
    public List<S3Documents> list();
}
