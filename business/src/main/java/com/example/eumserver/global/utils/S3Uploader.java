package com.example.eumserver.global.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.eumserver.global.error.exception.CustomException;
import com.example.eumserver.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Uploader {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public String uploadToS3(S3Path s3Path, MultipartFile multipartFile) throws IOException {
        File uploadFile = this.convert(multipartFile)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE));

        String storePath = s3Path.path + "/" + UUID.randomUUID();

        String imageUrl = putS3(uploadFile, storePath);

        removeNewFile(uploadFile);
        return imageUrl;
    }

    private String putS3(File uploadFile, String storePath) {
        amazonS3.putObject(new PutObjectRequest(bucket, storePath, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3.getUrl(bucket,storePath).toString();
    }

    private Optional<File> convert(MultipartFile  multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = UUID.randomUUID() + "_" + originalFilename;

        File convertFile = new File(System.getProperty("user.dir") + "/" + storeFileName);

        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(multipartFile.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }
    
    public enum S3Path {
        TEAM_IMAGE("/teams");

        final String path;

        S3Path(String path) {
            this.path = path;
        }
    }

}

