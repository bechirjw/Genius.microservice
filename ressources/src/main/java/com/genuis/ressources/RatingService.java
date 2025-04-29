package com.genuis.ressources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {
    @Autowired
    private RatingRepository ratingRepository;

    public List<CommentDto> getCommentsForResource(Long resourceId) {
        List<Rating> ratings = ratingRepository.findByResourceId(resourceId);
        System.out.println("Ratings trouvées pour la ressource " + resourceId + ": " + ratings.size());  // Log pour vérifier si des résultats sont trouvés
        return ratings.stream()
                .map(rating -> new CommentDto(rating.getUsername(), rating.getComment()))
                .toList();
    }
    public Rating save(RatingDto ratingDto) {
        Rating rating = new Rating();
        rating.setResourceId(ratingDto.getResourceId());
        rating.setUserId(ratingDto.getUserId());
        rating.setRating(ratingDto.getRating());
        rating.setComment(ratingDto.getComment());
        rating.setUsername(ratingDto.getUsername());
        return ratingRepository.save(rating);
    }

    public double getAverageRatingForResource(Long resourceId) {
        List<Rating> ratings = ratingRepository.findByResourceId(resourceId);
        return ratings.stream()
                .mapToInt(Rating::getRating)
                .average()
                .orElse(0.0);
    }
}

