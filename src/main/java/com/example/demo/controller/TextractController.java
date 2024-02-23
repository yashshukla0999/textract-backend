package com.example.demo.controller;

import com.example.demo.service.TextractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class TextractController {

    @Autowired
    private TextractService textractService;

    @PostMapping("/convert")
    public ResponseEntity< List<List<Map<String, String>>>> convertPdfToImages(@RequestPart("pdfFile") MultipartFile pdfFile) {
        try {
        	 List<List<Map<String, String>>> analysisResult = textractService.analyzePdf(pdfFile);
            return ResponseEntity.ok(analysisResult);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
