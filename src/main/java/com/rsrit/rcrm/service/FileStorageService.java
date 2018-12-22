package com.rsrit.rcrm.service;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.NoSuchElementException;

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
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@Service
public class FileStorageService {

    @Value("${aws.access.key}")
    private String awsAccessKey;
    @Value("${aws.secret.key}")
    private String awsSecretKey;

    private String bucketName = "rcrm-storage";
    private String path = "Documents/";
    private AWSCredentials credentials;
    private AmazonS3 s3Client;

    @PostConstruct
    public void init() {
        this.credentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
        this.s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_2).build();

    }

    // Save this document as fileName in defaultBucket
    public String saveToAws(MultipartFile multiPart, String fileName) throws IOException, MimeTypeException {
        // TODO Auto-generated method stub
        String awsFileName = path + fileName + getFileExtensionFromMimeType(multiPart.getContentType());

        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(multiPart.getSize());
        // s3client.putObject(bucketName, awsFileName, new File(path));
        s3Client.putObject(new PutObjectRequest(bucketName, awsFileName, multiPart.getInputStream(), meta));
        URL url = s3Client.getUrl(bucketName, awsFileName);
        return url.toString();
    }

    public static String getFileExtensionFromMimeType(String mimeType) throws MimeTypeException {
        MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
        MimeType mt = allTypes.forName(mimeType);
        return mt.getExtension();
    }

    public void deleteFromAws(String url) {
        AmazonS3URI uri = new AmazonS3URI(url);
        // s3Client.deleteObject(uri.getBucket(), uri.getKey());

        ObjectListing objectListing = s3Client.listObjects(bucketName);
        while (true) {
            Iterator<S3ObjectSummary> objIter = objectListing.getObjectSummaries().iterator();
            while (objIter.hasNext()) {
                String key = objIter.next().getKey();
                if (key.equals(uri.getKey()))
                    try {
                        s3Client.deleteObject(bucketName, key);
                    } catch (NoSuchElementException e) {
                        System.out.println("element " + key + " not found");
                    }
            }

            // If the bucket contains many objects, the listObjects() call
            // might not return all of the objects in the first listing. Check to
            // see whether the listing was truncated. If so, retrieve the next page of objects
            // and delete them.
            if (objectListing.isTruncated()) {
                objectListing = s3Client.listNextBatchOfObjects(objectListing);
            } else {
                break;
            }
        }
    }

    public void deleteAll() {
        ObjectListing objectListing = s3Client.listObjects(bucketName);
        while (true) {
            Iterator<S3ObjectSummary> objIter = objectListing.getObjectSummaries().iterator();
            while (objIter.hasNext()) {
                s3Client.deleteObject(bucketName, objIter.next().getKey());
            }

            // If the bucket contains many objects, the listObjects() call
            // might not return all of the objects in the first listing. Check to
            // see whether the listing was truncated. If so, retrieve the next page of objects
            // and delete them.
            if (objectListing.isTruncated()) {
                objectListing = s3Client.listNextBatchOfObjects(objectListing);
            } else {
                break;
            }
        }
    }
}
