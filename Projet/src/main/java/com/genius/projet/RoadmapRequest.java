package com.genius.projet;

import java.util.List;
import java.util.Map;

public class RoadmapRequest {
    private String description;
    private List<String> taches;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTaches() {
        return taches;
    }

    public void setTaches(List<String> taches) {
        this.taches = taches;
    }


}
