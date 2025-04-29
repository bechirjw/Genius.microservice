package com.genuis.ressources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @PostMapping
    public ResponseEntity<?> createRating(@RequestBody RatingDto ratingDto) {
        ratingService.save(ratingDto);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Rating enregistré !");
        return ResponseEntity.ok("Rating enregistré !");
    }

    @GetMapping("/average/{resourceId}")
    public double getAverageRating(@PathVariable Long resourceId) {
        return ratingService.getAverageRatingForResource(resourceId);
    }



    @GetMapping("/comments/{resourceId}")
    public ResponseEntity<?> getCommentsForResource(@PathVariable Long resourceId) {
        List<CommentDto> comments = ratingService.getCommentsForResource(resourceId);
        if (comments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Aucun commentaire trouvé pour cette ressource.");
        }
        return ResponseEntity.ok(comments);
    }
}
