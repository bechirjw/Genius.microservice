package com.genius.quizservice.repository;

import com.genius.quizservice.entity.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {
    @Query("SELECT COALESCE(SUM(tr.correctAnswers), 0) FROM TestResult tr")
    int sumCorrectAnswers();

    @Query("SELECT COALESCE(SUM(tr.totalQuestions - tr.correctAnswers), 0) FROM TestResult tr")
    int sumWrongAnswers();
}
