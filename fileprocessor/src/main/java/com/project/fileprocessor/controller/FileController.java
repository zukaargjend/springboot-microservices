package com.project.fileprocessor.controller;

import com.project.fileprocessor.entity.FileEntity;
import com.project.fileprocessor.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin/data")
public class FileController {

    private FileService fileService;

    public FileController(final FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> saveFileData(@RequestParam("file") MultipartFile file) throws IOException {
        String fileId = fileService.saveFileData(file);
        return ResponseEntity.ok("Excel file is being processed. Your File ID is: " + fileId);
    }

    @GetMapping()
    public List<FileEntity> getAllFiles() {
        return fileService.getAllFiles();
    }
}
