package com.genius.stageservice.service;

import com.genius.stageservice.entity.Stage;
import java.util.List;

public interface StageService {
    List<Stage> getAllStages();
    Stage getStageById(Long id);
    Stage createStage(Stage stage);
    Stage updateStage(Long id, Stage stage);
    void deleteStage(Long id);
}
