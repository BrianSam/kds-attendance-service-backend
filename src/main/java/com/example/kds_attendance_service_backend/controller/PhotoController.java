package com.example.kds_attendance_service_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/photos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PhotoController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.base-url}")
    private String baseUrl;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<String> uploadPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam("employeeId") Long employeeId
    ) throws IOException {

        // Validate file is an image
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.badRequest().body("Only image files are allowed");
        }

        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique file name
        // Format: photo_<employeeId>_<date>_<uuid>.jpg
        String fileExtension = getFileExtension(file.getOriginalFilename());
        String fileName = "photo_"
                + employeeId + "_"
                + LocalDate.now() + "_"
                + UUID.randomUUID().toString().substring(0, 8)
                + fileExtension;

        // Save file to disk
        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, file.getBytes());

        // Return the accessible URL
        String photoUrl = baseUrl + "/photos/" + fileName;
        return ResponseEntity.ok(photoUrl);
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return ".jpg"; // default
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
