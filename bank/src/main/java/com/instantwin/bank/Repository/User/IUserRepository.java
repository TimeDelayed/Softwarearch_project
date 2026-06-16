package com.instantwin.bank.Repository.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.instantwin.bank.Model.User.UserEntity;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Long>{
    
}
