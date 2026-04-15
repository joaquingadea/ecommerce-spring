package com.api.ecommerce.products.application;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {
    private final String uploadDir = "uploads/";

    public String saveFile(MultipartFile file){
        try {

            // random ID + nombre del archivo
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            // path = "uploads/fileName"
            Path path = Paths.get(uploadDir + fileName);

            // toma la carpeta padre y si no existe la crea
            Files.createDirectories(path.getParent());

            // escribe en el path guardado los bytes del archivo
            Files.write(path, file.getBytes());

            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
