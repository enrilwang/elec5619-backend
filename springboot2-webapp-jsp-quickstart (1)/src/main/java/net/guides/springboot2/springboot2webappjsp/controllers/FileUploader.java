package net.guides.springboot2.springboot2webappjsp.controllers;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

public class FileUploader {

    //Return store path if success
    //Return "fail" if fail

    public String fileUpload(MultipartFile file, String storePath) {
        //File upload path

        //Using original filename
        String storeName = file.getOriginalFilename();
        //New file store path
        File storeFile = new File(storePath + "/" + storeName);

        try {
            //Create directory if not exist
            if (!storeFile.getParentFile().exists()) {
                storeFile.getParentFile().mkdirs();
            }
            try {
                storeFile.createNewFile();
                file.transferTo(storeFile);
                return storeFile.getPath() + storeFile.getName();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return "fail";
            }
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            return "fail";
        }
    }
}
