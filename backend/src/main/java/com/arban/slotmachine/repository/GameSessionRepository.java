package com.arban.slotmachine.repository;

import com.arban.slotmachine.entity.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, Long> {
    Optional<GameSession> findByPlayerName(String playerName);
    List<GameSession> findTop10ByOrderByTotalWinningsDesc();
}
