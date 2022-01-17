package com.skillbox.socialnet.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StorageLoggingService {

    @Value("${aws.access.key}")
    private String accessKey;

    @Value("${aws.secret.key}")
    private String secretKey;

    private static final String BUCKET_NAME = "social-net-20-group";
    private static final String LOG_DIR = "logs";

    private AmazonS3 s3Client;

    @Scheduled(cron = "${log.files.scheduling.cron.expression}")
    public void updateLogFilesToCloud() throws FileNotFoundException {
        this.s3Client = getAmazonS3();
        deleteFilesFromCloud();
        saveLogFilesToCloud();
    }

    private File[] getLogFiles() {
        File logDir = new File(LOG_DIR);
        return logDir.listFiles();
    }

    private void saveLogFilesToCloud() throws FileNotFoundException {
        File[] files = getLogFiles();
        if(files != null & files.length > 0) {
            for(File file : files) {
                saveFileToCloud(file);
                emptyFile(file);
            }
        }
    }

    private void emptyFile(File file) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(file);
        writer.print("");
        writer.close();
    }

    private void saveFileToCloud(File file) {
        String fileName = LOG_DIR + "/" + file.getName();
        s3Client.putObject(new PutObjectRequest(BUCKET_NAME, fileName, file));
    }

    private void deleteFilesFromCloud() {
        List<String> fileNames = getAllFileNamesFromCloud();
        if(fileNames != null & fileNames.size() > 0) {
            for (String fileName : fileNames) {
                s3Client.deleteObject(BUCKET_NAME, fileName);
            }
        }
    }

    private List<String> getAllFileNamesFromCloud() {
        ObjectListing listing = s3Client.listObjects(BUCKET_NAME);
        return listing.getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
    }

    private AmazonS3 getAmazonS3() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder
                .standard()
                .withRegion(Regions.EU_CENTRAL_1)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

//    public void deleteBucket() {
//        this.s3Client = getAmazonS3();
//        String bucketName = "social-net-20-group-log-files";
//        try {
//            s3Client.deleteBucket("social-net-20-group-log-files");
//        } catch (AmazonServiceException e) {
//            System.err.println(e.getErrorMessage());
//        }
//
//        List<Bucket> buckets = s3Client.listBuckets();
//
//        for(Bucket bucket : buckets) {
//            System.out.println(bucket.getName());
//        }
//    }
//
//    public void createBucket(){
//        String bucketName = "social-net-20-group";
//
//        this.s3Client = getAmazonS3();
//
//        if(s3Client.doesBucketExistV2(bucketName)) {
//            System.out.println("Bucket name is not available."
//                    + " Try again with a different Bucket name.");
//            return;
//        } else {
//            System.out.println("Bucket name is available");
//        }
//
//        s3Client.createBucket(bucketName);
//
//        List<Bucket> buckets = s3Client.listBuckets();
//
//        for(Bucket bucket : buckets) {
//            System.out.println(bucket.getName());
//        }
//    }
}
