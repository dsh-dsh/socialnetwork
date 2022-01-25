package com.skillbox.socialnet.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.dto.FileDTO;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.mapper.DefaultRSMapper;
import com.skillbox.socialnet.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final AuthService authService;
    private final PersonRepository personRepository;

    public FileDTO saveImageToProfile(String type, MultipartFile multipartFile) throws IOException {
        Person activePerson = authService.getPersonFromSecurityContext();
        File file = new File("src/main/resources/targetFile.jpg");

        try (OutputStream os = new FileOutputStream(file)) {
            os.write(multipartFile.getBytes());
        }

        FileDTO fileDTO = new FileDTO();
        if (!multipartFile.isEmpty()) {
            String prevImg = "";
            if (activePerson.getPhoto() != null){
                String[] strings = activePerson.getPhoto().split("/");
                prevImg = strings[strings.length - 1];
            }
            activePerson.setPhoto(savePhotoInCloud(file, prevImg));
            personRepository.save(activePerson);
            fileDTO.setOwnerId(activePerson.getId());
            fileDTO.setFileName(multipartFile.getName());
            fileDTO.setRelativeFilePath(activePerson.getPhoto());
            fileDTO.setRawFileURL(null);
            fileDTO.setFileFormat(multipartFile.getContentType());
            fileDTO.setBytes(multipartFile.getBytes().length);
            fileDTO.setFileType(type);
        }
        return fileDTO;
    }

    private String savePhotoInCloud(File file, String prevImg) {
        String fileName = System.currentTimeMillis() + file.getName();
        Regions clientRegion = Regions.EU_CENTRAL_1;
        String bucketName = "jevaibucket/publicprefix";
        String fileObjKeyName = fileName;
        AWSCredentials awsCredentials =
                new BasicAWSCredentials("AKIAVAR2I7GKLP66SIHL", "W3dXfLlwvfj+E8ucH62wwgalYZufOXLwFx2yxWu+");
        AmazonS3 s3Client = AmazonS3ClientBuilder
                .standard()
                .withRegion(clientRegion)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
        s3Client.putObject(new PutObjectRequest(bucketName, fileObjKeyName, file));
        if (!prevImg.isEmpty()){
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, prevImg));
        }
        return "https://jevaibucket.s3.eu-central-1.amazonaws.com/publicprefix/" + fileObjKeyName;
    }

    private void deletePhotoFromCloud(String prevImg){
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
