package com.genius.stageservice.service;

import com.genius.stageservice.entity.Stage;
import com.genius.stageservice.repository.StageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StageServiceImpl implements StageService {

    @Autowired
    private StageRepository stageRepository;

    @Override
    public List<Stage> getAllStages() {
        return stageRepository.findAll();
    }

    @Override
    public Stage getStageById(Long id) {
        return stageRepository.findById(id).orElse(null);
    }

    @Override
    public Stage createStage(Stage stage) {
        return stageRepository.save(stage);
    }

    @Override
    public Stage updateStage(Long id, Stage updatedStage) {
        Stage existingStage = stageRepository.findById(id).orElse(null);
        if (existingStage != null) {
            existingStage.setType(updatedStage.getType());
            existingStage.setDomaine(updatedStage.getDomaine());
            existingStage.setDateDebut(updatedStage.getDateDebut());
            existingStage.setDateFin(updatedStage.getDateFin());
            existingStage.setEntreprise(updatedStage.getEntreprise());
            existingStage.setPayant(updatedStage.isPayant());

            // 🔁 Nouvelles propriétés
            existingStage.setEmail(updatedStage.getEmail());
            existingStage.setPhoneNumber(updatedStage.getPhoneNumber());
            existingStage.setDescription(updatedStage.getDescription());

            return stageRepository.save(existingStage);
        }
        return null;
    }

    @Override
    public void deleteStage(Long id) {
        stageRepository.deleteById(id);
    }
}
