package com.genius.forum.repository;

import com.genius.forum.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCommunityId(Long communityId);
    List<Post> findByIsReportedTrue();
}