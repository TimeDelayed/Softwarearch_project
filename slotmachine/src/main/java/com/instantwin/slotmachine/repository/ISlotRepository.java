package com.instantwin.slotmachine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.instantwin.slotmachine.model.SlotGameEntity;

@Repository
public interface ISlotRepository extends JpaRepository<SlotGameEntity, Long> {
    List<SlotGameEntity> findAllByUserId(Long userId);
}
