package com.three.d.tic.tac.toe.repository;

import java.util.List;
import java.util.Optional;

import com.three.d.tic.tac.toe.domain.GameEntity;
import com.three.d.tic.tac.toe.domain.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface GameRepository extends JpaRepository<GameEntity, Long> {

    List<GameEntity> findByUserEntityOrderByIdDesc(UserEntity userEntity);

    Optional<GameEntity> findByIdAndUserEntity(Long id, UserEntity userEntity);

    @Modifying
    @Transactional
    @Query("DELETE FROM GameEntity ge WHERE ge.nextTurn is null")
    void deleteByNextTurnNull();
}
