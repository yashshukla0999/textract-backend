package com.example.demo.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.textract.TextractClient;

@Service
public class TextractService {

    private static final String IMAGE_OUTPUT_DIRECTORY = "F:\\java\\DemoProject\\Texttract\\text\\images";

    private final TextractClient textractClient;

    public TextractService() {
        this.textractClient = TextractClient.builder().region(Region.US_EAST_1).build();
    }

    public  List<List<Map<String, String>>> analyzePdf(MultipartFile pdfFile) throws IOException {
        List<String> imagePaths = new ArrayList<>();
        List<List<Map<String, String>>> expenseListOfListOfMap = new ArrayList<>();
        HashMap<String, String> combinedExpenseMap = new HashMap<>();
        try (PDDocument document = PDDocument.load(pdfFile.getInputStream())) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
                String imagePath = saveImage(bufferedImage, page);
                imagePaths.add(imagePath);
                File imageFile = new File(imagePath);
                KeyValueService keyValueService = new KeyValueService();
                List<Map<String, String>> expenseMap = keyValueService.analyzeExpense(imageFile);
                expenseListOfListOfMap.add(expenseMap);
                //combinedExpenseMap.putAll(expenseMap);
            }
        }
        return expenseListOfListOfMap;
        //return convertMapToJson(combinedExpenseMap);
    }

    private String saveImage(BufferedImage image, int pageNumber) throws IOException {
        String imagePath = IMAGE_OUTPUT_DIRECTORY + File.separator + "page123_" + (pageNumber + 1) + ".png";
        File outputFile = new File(imagePath);
        javax.imageio.ImageIO.write(image, "png", outputFile);
        return imagePath;
    }

    private String convertMapToJson(HashMap<String, String> map) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(map);
    }
}
