package com.genius.forum.dto;

import com.genius.forum.model.VoteType;

public class VoteDTO {

    private Long postId;
    private VoteType voteType;

    public VoteDTO() {
    }

    public VoteDTO(Long postId, VoteType voteType) {
        this.postId = postId;
        this.voteType = voteType;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public VoteType getVoteType() {
        return voteType;
    }

    public void setVoteType(VoteType voteType) {
        this.voteType = voteType;
    }

    @Override
    public String toString() {
        return "VoteDTO{" +
                "postId=" + postId +
                ", voteType=" + voteType +
                '}';
    }
}
