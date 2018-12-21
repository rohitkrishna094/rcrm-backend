package com.rsrit.rcrm.service;

import java.io.IOException;
import java.net.URL;

import javax.annotation.PostConstruct;

import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class FileStorageService {

    @Value("${aws.access.key}")
    private String awsAccessKey;
    @Value("${aws.secret.key}")
    private String awsSecretKey;

    private String bucketName = "rcrm-storage";
    private String path = "Documents/";
    private AWSCredentials credentials;
    private AmazonS3 s3client;

    @PostConstruct
    public void init() {
        this.credentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
        this.s3client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_2).build();

    }

    // Save this document as fileName in defaultBucket
    public String saveToAws(MultipartFile multiPart, String fileName) throws IOException, MimeTypeException {
        // TODO Auto-generated method stub
        String awsFileName = path + fileName + getFileExtensionFromMimeType(multiPart.getContentType());

        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(multiPart.getSize());
        // s3client.putObject(bucketName, awsFileName, new File(path));
        s3client.putObject(new PutObjectRequest(bucketName, awsFileName, multiPart.getInputStream(), meta));
        URL url = s3client.getUrl(bucketName, awsFileName);
        return url.toString();
    }

    public static String getFileExtensionFromMimeType(String mimeType) throws MimeTypeException {
        MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
        MimeType mt = allTypes.forName(mimeType);
        return mt.getExtension();
    }

    public void deleteFromAws(String url) {
        AmazonS3URI uri = new AmazonS3URI(url);
        s3client.deleteObject(uri.getBucket(), uri.getKey());
    }

}
