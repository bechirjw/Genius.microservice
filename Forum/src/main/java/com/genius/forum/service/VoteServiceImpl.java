package com.genius.forum.service;

import com.genius.forum.dto.VoteDTO;
import com.genius.forum.model.Post;
import com.genius.forum.model.Vote;
import com.genius.forum.model.VoteType;
import com.genius.forum.repository.PostRepository;
import com.genius.forum.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private PostRepository postRepository;

    @Override
    public void vote(VoteDTO voteDTO) {
        Post post = postRepository.findById(voteDTO.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Vote vote = new Vote(voteDTO.getVoteType(), post);
        voteRepository.save(vote);
    }

    @Override
    public Map<String, Integer> getVoteCount(Long postId) {
        List<Vote> votes = voteRepository.findByPostId(postId);

        int upvotes = (int) votes.stream().filter(v -> v.getVoteType() == VoteType.UPVOTE).count();
        int downvotes = (int) votes.stream().filter(v -> v.getVoteType() == VoteType.DOWNVOTE).count();

        Map<String, Integer> result = new HashMap<>();
        result.put("upvotes", upvotes);
        result.put("downvotes", downvotes);

        return result;
    }
}