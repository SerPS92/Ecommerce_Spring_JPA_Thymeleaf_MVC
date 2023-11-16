package com.example.ecommerce.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UploadFileService {

    private final String folder = "src/main/resources/static/images//";

    public String saveImage(MultipartFile file) throws IOException{
        if(!file.isEmpty()){
            byte[] bytes = file.getBytes();
            Path path = Paths.get(folder + file.getOriginalFilename());
            Files.write(path, bytes);
            return file.getOriginalFilename();
        }
        return "default.jpg";
    }

    public String saveImage2(MultipartFile file2) throws IOException{
        if(!file2.isEmpty()){
            byte[] bytes = file2.getBytes();
            Path path = Paths.get(folder + file2.getOriginalFilename());
            Files.write(path, bytes);
            return file2.getOriginalFilename();
        }
        return "default.jpg";
    }

    public String saveImage3(MultipartFile file3) throws IOException{
        if(!file3.isEmpty()){
            byte[] bytes = file3.getBytes();
            Path path = Paths.get(folder + file3.getOriginalFilename());
            Files.write(path, bytes);
            return file3.getOriginalFilename();
        }
        return "default.jpg";
    }

    public void deleteImage(String name){
        String ruta = "src/main/resources/static/images//";
        File file = new File(ruta + name);
        file.delete();
    }
}
