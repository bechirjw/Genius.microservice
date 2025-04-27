package com.genius.forum.repository;

import com.genius.forum.model.Community;
import com.genius.forum.model.Membership;
import com.genius.forum.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    boolean existsByUserAndCommunity(User user, Community community);
}