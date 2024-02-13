
package com.example.demo.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.model.Block;
import com.amazonaws.services.textract.model.DetectDocumentTextRequest;
import com.amazonaws.services.textract.model.DetectDocumentTextResult;
import com.amazonaws.services.textract.model.Document;
import com.example.demo.service.TextractService;

@RestController
public class TextractController {

    private static final Logger LOGGER = Logger.getLogger(TextractController.class.getName());

    @Autowired
    private TextractService textractService;

    @PostMapping("/extract-fields")
    public ResponseEntity<Map<String, String>> extractFields(@RequestParam("file") MultipartFile file) {
        try {
            // Call the extractFields method to extract all fields from the provided document
            Map<String, String> extractedFields = textractService.extractFields(file);
            LOGGER.log(Level.INFO, "Extracted fields: {0}", extractedFields);
            return new ResponseEntity<>(extractedFields, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error extracting fields", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
