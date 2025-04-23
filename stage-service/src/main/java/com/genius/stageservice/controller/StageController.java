package com.genius.stageservice.controller;

import com.genius.stageservice.entity.Stage;
import com.genius.stageservice.service.StageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stages")
public class StageController {

    @Autowired
    private StageService stageService;

    @GetMapping
    public List<Stage> getAllStages() {
        return stageService.getAllStages();
    }

    @GetMapping("/{id}")
    public Stage getStageById(@PathVariable Long id) {
        return stageService.getStageById(id);
    }

    @PostMapping
    public Stage createStage(@RequestBody Stage stage) {
        return stageService.createStage(stage);
    }

    @PutMapping("/{id}")
    public Stage updateStage(@PathVariable Long id, @RequestBody Stage stage) {
        return stageService.updateStage(id, stage);
    }

    @DeleteMapping("/{id}")
    public void deleteStage(@PathVariable Long id) {
        stageService.deleteStage(id);
    }
}
