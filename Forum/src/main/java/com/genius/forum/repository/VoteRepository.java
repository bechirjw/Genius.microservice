package com.genius.forum.repository;

import com.genius.forum.model.Post;
import com.genius.forum.model.User;
import com.genius.forum.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByUserAndPost(User user, Post post);
    int countByPostAndType(Post post, Vote.VoteType type);
}

