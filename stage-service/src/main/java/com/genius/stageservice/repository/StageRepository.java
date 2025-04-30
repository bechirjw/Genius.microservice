package com.genius.stageservice.repository;

import com.genius.stageservice.entity.Stage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StageRepository extends JpaRepository<Stage, Long> {
}
