package com.project.fileprocessor.service;

import com.project.fileprocessor.dto.SendSimpleEmailEvent;
import com.project.fileprocessor.entity.FileEntity;
import com.project.fileprocessor.producer.RabbitMQProducer;
import com.project.fileprocessor.repository.FileRepository;
import com.github.pjfanning.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class FileService {

    private static final int BATCH_SIZE = 1000;
    private final FileRepository fileRepository;
    private final FileService fileServiceSelf;
    private final RabbitMQProducer rabbitMQProducer;

    public FileService(final FileRepository fileRepository, @Lazy FileService fileServiceSelf, RabbitMQProducer rabbitMQProducer) {
        this.fileRepository = fileRepository;
        this.fileServiceSelf = fileServiceSelf;
        this.rabbitMQProducer = rabbitMQProducer;
    }

    public String saveFileData(MultipartFile file) throws IOException {
        String id = UUID.randomUUID().toString();
        fileServiceSelf.uploadFile(id, file.getInputStream());

        System.out.println(id);

        return id;
    }

    @Async
    @Transactional
    protected void uploadFile(String id, InputStream file) throws IOException {
        // Increase POI's byte array limit for large files (200MB)
        IOUtils.setByteArrayMaxOverride(200_000_000);

        List<FileEntity> batches = new ArrayList<>(BATCH_SIZE);

        // Use streaming reader to avoid loading entire file into memory
        Workbook workbook = StreamingReader.builder()
                .rowCacheSize(100)    // Cache only 100 rows at a time
                .bufferSize(4096)      // 4KB buffer
                .open(file);

        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();

        AtomicLong rowId = new AtomicLong(1);
        List<String> headers = new ArrayList<>();
        boolean[] isFirstRow = {true};

        // Process all rows with streaming
        for (Row row : sheet) {
            // First row is header
            if (isFirstRow[0]) {
                row.forEach(cell -> headers.add(dataFormatter.formatCellValue(cell)));
                isFirstRow[0] = false;
                continue;
            }

            // Process data rows
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileId(id);
            fileEntity.setRowId(rowId.getAndIncrement());

            StringBuilder jsonBuilder = new StringBuilder("{");
            for (int i = 0; i < headers.size(); i++) {
                String columnName = headers.get(i);
                String cellValue = dataFormatter.formatCellValue(row.getCell(i));
                cellValue = cellValue.replace("\"", "\\\"");

                jsonBuilder.append("\"").append(columnName).append("\":\"").append(cellValue).append("\"");

                if (i < headers.size() - 1) {
                    jsonBuilder.append(",");
                }
            }
            jsonBuilder.append("}");

            fileEntity.setContent(jsonBuilder.toString());
            batches.add(fileEntity);

            if (batches.size() >= BATCH_SIZE) {
                fileRepository.saveAll(new ArrayList<>(batches));
                batches.clear();
                System.gc();
            }
        }

        //Save remaining records
        if(!batches.isEmpty()){
            fileRepository.saveAll(batches);
            batches.clear();
        }
        workbook.close();
        System.gc();

        SendSimpleEmailEvent sendEmail = new SendSimpleEmailEvent(
                "emails...",
                "File Upload Completed",
                """
                Hello Sir/Madam,

                Your file is processed.

                This is an automated message — please do not reply to this email.

                Best regards,
                Zuka Team
                """
        );
        rabbitMQProducer.sendMessage(sendEmail);

        System.out.println("Done!");
    }

    public List<FileEntity> getAllFiles() {
        return fileRepository.findAll();
    }
}
