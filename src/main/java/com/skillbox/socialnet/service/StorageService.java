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
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StorageService {

    private AuthService authService;

    public DefaultRS<?> saveImageToProfile(String type, MultipartFile multipartFile) throws IOException {
        Person activePerson = authService.getPersonFromSecurityContext();
        File file = new File("file.jpg");
        multipartFile.transferTo(file);
        FileDTO fileDTO = new FileDTO();
        if (!multipartFile.isEmpty()) {
            String prevImg = "";
            if (activePerson.getPhoto() != null){
                String[] strings = activePerson.getPhoto().split("/");
                prevImg = strings[strings.length - 1];
            }
            activePerson.setPhoto(savePhotoInCloud(file, type, prevImg));
            fileDTO.setOwnerId(activePerson.getId());
            fileDTO.setFileName(multipartFile.getName());
            fileDTO.setRelativeFilePath(activePerson.getPhoto());
            fileDTO.setRawFileURL(null);
            fileDTO.setFileFormat(multipartFile.getContentType());
            fileDTO.setBytes(multipartFile.getBytes().length);
            fileDTO.setFileType(type);
        }
        return DefaultRSMapper.of(fileDTO);
    }

    private String savePhotoInCloud(File file, String type, String prevImg) {
        String fileName = System.currentTimeMillis() + file.getName();
        Regions clientRegion = Regions.EU_CENTRAL_1;
        String bucketName = "jevaibucket/publicprefix";
        String fileObjKeyName = fileName + type;
        AWSCredentials awsCredentials =
                new BasicAWSCredentials("AKIAVAR2I7GKLP66SIHL", "W3dXfLlwvfj+E8ucH62wwgalYZufOXLwFx2yxWu+");
        AmazonS3 s3Client = AmazonS3ClientBuilder
                .standard()
                .withRegion(clientRegion)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
        s3Client.putObject(new PutObjectRequest(bucketName, fileObjKeyName, file));
        if (prevImg.isEmpty()){
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, prevImg));
        }
        return "https://jevaibucket.s3.eu-central-1.amazonaws.com/publicprefix/" + fileObjKeyName;
    }
}
