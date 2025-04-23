package com.genius.quizservice.service;

import com.genius.quizservice.repository.QuestionRepository;
import com.genius.quizservice.repository.TestRepository;
import com.genius.quizservice.repository.TestResultRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import com.genius.quizservice.dto.*;
import com.genius.quizservice.entity.Question;
import com.genius.quizservice.entity.Test;

import com.genius.quizservice.entity.TestResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private TestRepository testRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestResultRepository testResultRepository;

    @Autowired
    private EmailService emailService;// Inject EmailService

    @Autowired
    private AIQuestionGeneratorService aiQuestionGeneratorService;


    public testDTO createTest(testDTO dto) {
        Test test = new Test();

        test.setTitle(dto.getTitle());
        test.setDescription(dto.getDescription());
        test.setTime(dto.getTime());

        return testRepository.save(test).getDto();
    }

    public QuestionDTO addQuestionInTest(QuestionDTO dto) {
        Optional<Test> optionalTest = testRepository.findById(dto.getId());

        if (optionalTest.isPresent()) {
            Question question = new Question();

            question.setTest(optionalTest.get());
            question.setQuestionText(dto.getQuestionText());
            question.setOptionA(dto.getOptionA());
            question.setOptionB(dto.getOptionB());
            question.setOptionC(dto.getOptionC());
            question.setOptionD(dto.getOptionD());
            question.setCorrectOption(dto.getCorrectOption());

            return questionRepository.save(question).getDto();
        }

        throw new EntityNotFoundException("Test Not Found");
    }

    public List<testDTO> getAllTests() {
        return testRepository.findAll().stream()
                .peek(test -> test.setTime(test.getQuestions().size() * test.getTime()))
                .map(Test::getDto)
                .collect(Collectors.toList());
    }

    public TestDetailsDTO getAllQuestionsByTest(Long id) {
        Optional<Test> optionalTest = testRepository.findById(id);
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();

        if (optionalTest.isPresent()) {
            testDTO testDTO = optionalTest.get().getDto();
            testDTO.setTime(testDTO.getTime() * optionalTest.get().getQuestions().size());

            testDetailsDTO.setTestDTO(testDTO);
            testDetailsDTO.setQuestions(
                    optionalTest.get().getQuestions().stream()
                            .map(Question::getDto)
                            .toList()
            );
            return testDetailsDTO;
        }

        return testDetailsDTO;
    }

    public TestResultDTO submitTest(SubmitTestDTO request) {
        try {
            // Log the incoming data for debugging
            System.out.println("Received SubmitTestDTO: " + request);

            // Fetch the test
            Test test = testRepository.findById(request.getTestId())


                    .orElseThrow(() -> new EntityNotFoundException("Test not found"));

            System.out.println("Fetched Test: " + test);

            int correctAnswers = 0;

            for (QuestionResponse response : request.getResponses()) {
                Question question = questionRepository.findById(response.getQuestionId())
                        .orElseThrow(() -> new EntityNotFoundException("Question not found"));

                System.out.println("Evaluating Question: " + question.getQuestionText());

                // Check if the answer is correct
                if (question.getCorrectOption().equals(response.getSelectedOption())) {
                    correctAnswers++;
                }
            }

            int totalQuestions = test.getQuestions().size();
            double percentage = (double) correctAnswers / totalQuestions * 100;

            // Log the result before saving
            System.out.println("Calculated Result - Correct: " + correctAnswers + ", Total: " + totalQuestions + ", Percentage: " + percentage);

            // Save the test result
            TestResult testResult = new TestResult();
            testResult.setTest(test);
            testResult.setTotalQuestions(totalQuestions);
            testResult.setCorrectAnswers(correctAnswers);
            testResult.setPercentage(percentage);

            // Save to DB and return the DTO
            TestResult savedTestResult = testResultRepository.save(testResult);
            System.out.println("Test Result Saved: " + savedTestResult);
            //Send email after the test is submitted
            String recipientEmail = "yayay9676@gmail.com";  // Replace with the actual user email
            String subject = "Quiz Submission Result";
            String body = "Congratulations, your quiz has been submitted successfully! You scored: " + percentage + "%";

            // Send email notification
            emailService.sendEmail(recipientEmail, subject, body);


            return savedTestResult.getDto();

        } catch (Exception e) {
            // Log any errors for debugging
            System.err.println("Error while submitting test: " + e.getMessage());
            throw e; // Re-throw or handle accordingly
        }
    }

    public List<TestResultDTO> getAllTestResults() {
        return testResultRepository.findAll().stream()
                .map(TestResult::getDto)
                .collect(Collectors.toList());
    }

    public void deleteTest(Long id) {
        Optional<Test> optionalTest = testRepository.findById(id);

        if (optionalTest.isPresent()) {
            testRepository.delete(optionalTest.get());
        } else {
            throw new EntityNotFoundException("Test with id " + id + " not found");
        }
    }












}
