package org.avokado2.sp.dao;


import org.avokado2.sp.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity findById(int id);
    List<UserEntity> findByLogin(String login);
}
