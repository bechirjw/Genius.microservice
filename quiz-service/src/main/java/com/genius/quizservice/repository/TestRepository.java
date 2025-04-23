package com.genius.quizservice.repository;

import com.genius.quizservice.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestRepository extends JpaRepository<Test, Long> {
    Optional<Test> findById(Long id);

}
