package com.brain.passwd.db;

import com.brain.passwd.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Integer> {

    Group findByGid(int id);
}
