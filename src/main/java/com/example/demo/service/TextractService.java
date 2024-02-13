package com.example.demo.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.model.Block;
import com.amazonaws.services.textract.model.DetectDocumentTextRequest;
import com.amazonaws.services.textract.model.DetectDocumentTextResult;
import com.amazonaws.services.textract.model.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TextractService {

    @Autowired
    private AmazonTextract textractClient;
    public Map<String, String> extractFields(MultipartFile file) {
        // Read file content into ByteBuffer
        ByteBuffer imageBytes;
        try {
            imageBytes = ByteBuffer.wrap(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Error reading file", e);
        }

        // Create request to detect document text
        DetectDocumentTextRequest request = new DetectDocumentTextRequest()
                .withDocument(new Document().withBytes(imageBytes));

        // Call AWS Textract service
        DetectDocumentTextResult result;
        try {
            result = textractClient.detectDocumentText(request);
        } catch (SdkClientException e) {
            throw new RuntimeException("Error communicating with AWS Textract service", e);
        }

        // Extract text blocks and parse into fields
        Map<String, String> extractedFields = new HashMap<>();
        List<Block> blocks = result.getBlocks();
        for (Block block : blocks) {
            if ("LINE".equals(block.getBlockType())) {
                // Split block text into field name and value
                String[] parts = block.getText().split(":", 2);
                if (parts.length == 2) {
                    String fieldName = parts[0].trim();
                    String fieldValue = parts[1].trim();
                    // Add field name and value to the map
                    extractedFields.put(fieldName, fieldValue);
                }
            }
        }
        return extractedFields;
    }
}
