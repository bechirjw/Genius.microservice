package com.genius.quizservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genius.quizservice.dto.testDTO;
import com.genius.quizservice.service.AIQuestionGeneratorService;
import jakarta.persistence.EntityNotFoundException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.genius.quizservice.dto.QuestionDTO;
import com.genius.quizservice.dto.SubmitTestDTO;
import com.genius.quizservice.service.TestService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private TestService testService;

    @Autowired
    public AIQuestionGeneratorService aiQuestionGeneratorService;



    @PostMapping()
    public ResponseEntity<?> createTest(@RequestBody testDTO dto) {
        try {
            return new ResponseEntity<>(testService.createTest(dto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/question")
    public ResponseEntity<?> addQuestionInTest(@RequestBody QuestionDTO dto) {
        try {
            // Call the service method to add the question and return a successful response
            return new ResponseEntity<>(testService.addQuestionInTest(dto), HttpStatus.CREATED);
        } catch (Exception e) {
            // Catch any exception and return a bad request with the error message
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping()
    public ResponseEntity<?> getAllTests() {
        try {
            return new ResponseEntity<>(testService.getAllTests(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getAllQuestions(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(testService.getAllQuestionsByTest(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/submit-test")
    public ResponseEntity<?> submitTest(@RequestBody SubmitTestDTO dto) {
        try {
            return new ResponseEntity<>(testService.submitTest(dto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/test-result")
    public ResponseEntity<?> getAllTestResults() {
        try {
            return new ResponseEntity<>(testService.getAllTestResults(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }


    }
        @DeleteMapping("/test/{id}")
        public ResponseEntity<?> deleteTest(@PathVariable Long id) {
            try {
                // Call the service method to delete the test and return a successful response
                testService.deleteTest(id);
                return new ResponseEntity<>("Test deleted successfully", HttpStatus.OK);
            } catch (EntityNotFoundException e) {
                // Handle case where the test is not found
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }

    @PostMapping("/generate-questions")
    public ResponseEntity<?> generateQuestions(@RequestBody testDTO dto) {
        try {
            String description = dto.getDescription();
            String generatedQuestionsJson = aiQuestionGeneratorService.generateQuestions(description);

            // Safely parse the response from Flask
            ObjectMapper mapper = new ObjectMapper();
            JsonNode responseNode = mapper.readTree(generatedQuestionsJson);

            return ResponseEntity.ok(responseNode);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating questions: " + e.getMessage());
        }
    }
    @GetMapping("/api/test-ping-user")
    public String testUserService() {
        RestTemplate restTemplate = new RestTemplate();
        String userServiceUrl = "http://localhost:5300/api/users/ping"; // user-service local
        return restTemplate.getForObject(userServiceUrl, String.class);
    }



}













