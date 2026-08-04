package com.instantwin.bank.repository.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.instantwin.bank.model.User.UserEntity;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Long>{
    
}
