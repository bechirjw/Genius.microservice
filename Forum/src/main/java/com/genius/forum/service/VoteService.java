package com.genius.forum.service;

import com.genius.forum.dto.VoteDTO;

import java.util.Map;

public interface VoteService {
    void vote(VoteDTO voteDTO);
    Map<String, Integer> getVoteCount(Long postId);
}