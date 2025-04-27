package com.genuis.categories;

import com.genuis.categories.Like;
import com.genuis.categories.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/likes")

public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping("/like")
    public Like likeRessource(@RequestParam Long userId, @RequestParam Long ressourceId) {
        return likeService.likeRessource(userId, ressourceId);
    }

    @DeleteMapping("/unlike")
    public void unlikeRessource(@RequestParam Long userId, @RequestParam Long ressourceId) {
        likeService.unlikeRessource(userId, ressourceId);
    }

    @GetMapping("/user/{userId}")
    public List<Like> getLikesByUser(@PathVariable Long userId) {
        return likeService.getLikesByUser(userId);
    }
}
