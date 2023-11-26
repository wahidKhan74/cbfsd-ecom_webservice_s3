package com.simplilearn.ecomorg.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.simplilearn.ecomorg.model.S3Documents;
import com.simplilearn.ecomorg.repository.S3DocumentRepository;
import com.simplilearn.ecomorg.service.AmazonS3Service;
import com.simplilearn.ecomorg.service.S3DocumentService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class S3DocumentServiceImp implements S3DocumentService{
	
	@Autowired
    private AmazonS3Service amazonS3Service;

    @Autowired
    private S3DocumentRepository documentRepository;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;


	@Override
	public void upload(MultipartFile file) throws IOException {
		 if (file.isEmpty())
	            throw new IllegalStateException("Cannot upload empty file");

	        Map<String, String> metadata = new HashMap<>();
	        metadata.put("Content-Type", file.getContentType());
	        metadata.put("Content-Length", String.valueOf(file.getSize()));

	        String path = String.format("%s/%s", bucketName, UUID.randomUUID());
	        String fileName = String.format("%s", file.getOriginalFilename());

	        // Uploading file to s3
	        PutObjectResult putObjectResult = amazonS3Service.upload(
	                path, fileName, Optional.of(metadata), file.getInputStream());

	        // Saving metadata to db
	        documentRepository.save(new S3Documents(fileName, path, putObjectResult.getMetadata().getVersionId()));
		
	}

	@Override
	public S3Object download(int id) {
		S3Documents fileMeta = documentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        return amazonS3Service.download(fileMeta.getFilePath(),fileMeta.getFileName());
	}

	@Override
	public List<S3Documents> list() {
		 List<S3Documents> metas = new ArrayList<>();
		 documentRepository.findAll().forEach(metas::add);
	     return metas;
	}

}
