package com.instantwin.roulette.repostitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.instantwin.roulette.Model.GameEntity;

@Repository
public interface IGameRepository extends JpaRepository<GameEntity, Long> {

    List<GameEntity> findByUserId(long userId);
}