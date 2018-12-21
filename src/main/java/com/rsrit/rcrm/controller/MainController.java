package com.rsrit.rcrm.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@RestController
@RequestMapping("/test")
public class MainController {

    @Value("${aws.access.key}")
    private String awsAccessKey;
    @Value("${aws.secret.key}")
    private String awsSecretKey;

    @GetMapping(value = "/{method}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<String> test(@PathVariable String method) {

        AWSCredentials credentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
        AmazonS3 s3client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_2).build();

        Random rand = new Random();
        String bucketName = "rcrm-storage";
        String path = "C:\\Users\\Rohit Krishna\\Desktop\\todo.txt";
        String fileNameInAws = "Document/hello" + rand.nextInt(100) + ".txt";
        if (method.equals("put"))
            s3client.putObject(bucketName, fileNameInAws, new File(path));
        else if (method.equals("delete"))
            s3client.deleteObject(bucketName, fileNameInAws);
        // return results
        ObjectListing objectListing = s3client.listObjects(bucketName);
        List<String> results = new ArrayList<>();
        for (S3ObjectSummary os : objectListing.getObjectSummaries()) {
            // results.add(os);
            String url = s3client.getUrl(bucketName, os.getKey()).toExternalForm();
            results.add(url);
        }

        return results;
    }

}
