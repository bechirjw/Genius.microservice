package com.genius.forum.repository;

import com.genius.forum.model.Community;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    List<Community> findByCreatedBy_Id(Long userId);


}
