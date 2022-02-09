package com.skillbox.socialnet.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StorageLoggingService {

    @Value("${aws.access.key}")
    private String accessKey;

    @Value("${aws.secret.key}")
    private String secretKey;

    private static final String BUCKET_NAME = "social-net-20-group";
    private static final String LOG_DIR = "logs";
    private static final long EXPIRATION_PERIOD = 30L*24*60*60*1000;

    private AmazonS3 s3Client;

    @Scheduled(cron = "${log.files.scheduling.cron.expression}")
    public void updateLogFilesToCloud() {
        this.s3Client = getAmazonS3();
        List<Path> paths = saveLogFilesToCloud();
        deleteEmptyLogDirs();
        deleteOldFilesFromCloud();
    }

    private AmazonS3 getAmazonS3() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder
                .standard()
                .withRegion(Regions.EU_CENTRAL_1)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    private List<Path> saveLogFilesToCloud() {
        List<Path> paths = getLogFilePaths(Paths.get(LOG_DIR));
        if(paths != null && paths.size() > 0) {
            for(Path path : paths) {
                saveLogFileToCloud(path);
            }
        }
        return paths;
    }

    private List<Path> getLogFilePaths(Path path) {
        try (Stream<Path> paths = Files.walk(path)) {
            return paths.filter(Files::isRegularFile).collect(Collectors.toList());
        } catch (IOException e) {
            return List.of();
        }
    }

    private void saveLogFileToCloud(Path path) {
        String fileName = path.toString();
        File file = path.toFile();
        s3Client.putObject(new PutObjectRequest(BUCKET_NAME, fileName, file));
    }


    private void deleteEmptyLogDirs() {
        File logDir = new File(LOG_DIR);
        File[] logDirs = logDir.listFiles();
        if(logDirs != null) {
            for (File nextDir : logDirs) {
                try(Stream<Path> paths = Files.walk(nextDir.toPath())) {
                    paths.map(Path::toFile)
                            .filter(File::isDirectory)
                            .filter(this::isEmptyDir)
                            .forEach(File::delete);
                } catch (IOException ignored) {}
            }
        }
    }

    private boolean isEmptyDir(File dir) {
        return Objects.requireNonNull(dir.listFiles()).length == 0;
    }

    private void deleteOldFilesFromCloud() {
        long expirationTime = new Date().getTime() - EXPIRATION_PERIOD;
        List<String> fileNames = getOldFilesFromCloud(expirationTime);
        if(fileNames != null & fileNames.size() > 0) {
            for (String fileName : fileNames) {
                s3Client.deleteObject(BUCKET_NAME, fileName);
            }
        }
    }

    private List<String> getOldFilesFromCloud(long expirationTime) {
        ObjectListing listing = s3Client.listObjects(BUCKET_NAME);
        return listing.getObjectSummaries().stream()
                .filter(o -> o.getLastModified().getTime() < expirationTime)
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
    }

    private void readFilesFromBucket(){
        System.out.println("files in bucket");
        ObjectListing listing = s3Client.listObjects(BUCKET_NAME);
        listing.getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .forEach(System.out::println);
    }

    private void deleteAllFilesFromCloud() {
        List<String> fileNames = s3Client.listObjects(BUCKET_NAME)
                .getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
        if(null != fileNames & fileNames.size() > 0) {
            for (String fileName : fileNames) {
                s3Client.deleteObject(BUCKET_NAME, fileName);
            }
        }
    }




    public void deleteBucket() {
        this.s3Client = getAmazonS3();
        String bucketName = "social-net-20-group-log-files";
        try {
            s3Client.deleteBucket("social-net-20-group-log-files");
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
        }
        List<Bucket> buckets = s3Client.listBuckets();
        for(Bucket bucket : buckets) {
            System.out.println(bucket.getName());
        }
    }

    public void createBucket(){
        String bucketName = "social-net-20-group";
        this.s3Client = getAmazonS3();
        if(s3Client.doesBucketExistV2(bucketName)) {
            System.out.println("Bucket name is not available."
                    + " Try again with a different Bucket name.");
            return;
        } else {
            System.out.println("Bucket name is available");
        }
        s3Client.createBucket(bucketName);
        List<Bucket> buckets = s3Client.listBuckets();
        for(Bucket bucket : buckets) {
            System.out.println(bucket.getName());
        }
    }
}
