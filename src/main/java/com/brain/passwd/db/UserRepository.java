package com.brain.passwd.db;

import com.brain.passwd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUid(int id);
}
