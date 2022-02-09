package com.skillbox.socialnet.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;

public class Cloud {
    private Cloud() {
    }

    public static String savePhotoInCloud(File file, String prevImg) {
        String fileName = System.currentTimeMillis() + file.getName();
        Regions clientRegion = Regions.EU_CENTRAL_1;
        String bucketName = "jevaibucket/publicprefix";
        AWSCredentials awsCredentials =
                new BasicAWSCredentials("AKIAVAR2I7GKLP66SIHL", "W3dXfLlwvfj+E8ucH62wwgalYZufOXLwFx2yxWu+");
        AmazonS3 s3Client = AmazonS3ClientBuilder
                .standard()
                .withRegion(clientRegion)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, file));
        if (!prevImg.isEmpty()){
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, prevImg));
        }
        return "https://jevaibucket.s3.eu-central-1.amazonaws.com/publicprefix/" + fileName;
    }

    public static void deletePhotoFromCloud(String prevImg){
        Regions clientRegion = Regions.EU_CENTRAL_1;
        String bucketName = "jevaibucket/publicprefix";
        AWSCredentials awsCredentials =
                new BasicAWSCredentials("AKIAVAR2I7GKLP66SIHL", "W3dXfLlwvfj+E8ucH62wwgalYZufOXLwFx2yxWu+");
        AmazonS3 s3Client = AmazonS3ClientBuilder
                .standard()
                .withRegion(clientRegion)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
        s3Client.deleteObject(new DeleteObjectRequest(bucketName, prevImg));
    }
}
