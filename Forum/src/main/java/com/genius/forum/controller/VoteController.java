package com.genius.forum.controller;

import com.genius.forum.dto.VoteDTO;
import com.genius.forum.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/votes")
//@CrossOrigin(origins = "*") // à adapter selon ton frontend
public class VoteController {

    @Autowired
    private VoteService voteService;

    // Ajouter un vote (upvote ou downvote)
    @PostMapping
    public void vote(@RequestBody VoteDTO voteDTO) {
        voteService.vote(voteDTO);
    }

    // Récupérer les compteurs de votes pour un post
    @GetMapping("/{postId}")
    public Map<String, Integer> getVoteCount(@PathVariable Long postId) {
        return voteService.getVoteCount(postId);
    }
}