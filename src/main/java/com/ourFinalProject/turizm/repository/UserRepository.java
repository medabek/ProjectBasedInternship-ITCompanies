package com.ourFinalProject.turizm.repository;

import com.ourFinalProject.turizm.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ourFinalProject.turizm.model.User;

import java.util.HashSet;
import java.util.Set;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    Set<User> findAllByRoles(HashSet<Role> roles);
}