package com.example.faewoomall.service;

import com.example.faewoomall.domain.UploadFile;
import com.example.faewoomall.repository.UploadFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStoreService {

    private final UploadFileRepository uploadFileRepository;

    @Value("${file.dir}")
    private String fileDir;

    //파일 전체 경로 반환
    public String getFullPath(String fileName) {

        return fileDir + fileName;
    }

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFile) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile file : multipartFile) {
            if (!file.isEmpty()) {
                storeFileResult.add(storeFile(file));
            }
        }
        return storeFileResult;
    }

    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFileName = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFileName);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));
        UploadFile uploadFile = new UploadFile();
        uploadFile.setUploadFileName(originalFileName);
        uploadFile.setStoredFileName(storeFileName);
        uploadFileRepository.save(uploadFile);
        return uploadFile;
    }

    public String createStoreFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        return uuid + '.' + extractExt(originalFilename);
    }

    public String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf('.');
        return originalFilename.substring(pos + 1);
    }


}
