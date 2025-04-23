package com.genius.quizservice.service;

import com.genius.quizservice.dto.*;

import java.util.List;

public interface TestService {
    public testDTO createTest(testDTO dto);
    public QuestionDTO addQuestionInTest(QuestionDTO dto);

    public List<testDTO> getAllTests();

    public TestDetailsDTO getAllQuestionsByTest(Long id);

    public TestResultDTO submitTest(SubmitTestDTO request);

    public List<TestResultDTO> getAllTestResults();
    public void deleteTest(Long id);
}
