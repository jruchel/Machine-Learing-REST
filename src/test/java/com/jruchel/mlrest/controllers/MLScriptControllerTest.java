package com.jruchel.mlrest.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class MLScriptControllerTest extends ControllerTest {

    @Autowired
    private MLScriptController mlScriptController;

    @Test
    public void algorithms() {
        List<String> expected = Arrays.asList("linear-regression", "k-nearest-neighbours");
        ResponseEntity<List<String>> response = mlScriptController.getAlgorithms();
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expected, response.getBody());
    }

    @Test
    public void studentsPerformanceLinearRegressionMathScoreNoSave() throws IOException {
        InputStream dataStream = new ClassPathResource("data/StudentsPerformance.csv").getInputStream();
        MultipartFile mockMultipartFile = new MockMultipartFile("csv", dataStream);
        assertLinearRegression(mockMultipartFile, ";", "math score", 86, -11, 7, false, "");
    }


    private void assertLinearRegression(
            MultipartFile dataFile,
            String separator,
            String predicting,
            double accuracy,
            double intercept,
            int coeffsSize,
            boolean save,
            String savename) throws IOException, NullPointerException {


    }

    private void assertWithinRange(double value, double start, double end) {
        assertTrue(value >= start && value <= end);
    }


}