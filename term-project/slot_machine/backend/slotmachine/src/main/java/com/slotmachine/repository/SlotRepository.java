package com.slotmachine.repository;
import com.slotmachine.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Long> {

    // Fetch sessions ordered by win count descending (leaderboard)
    @Query("SELECT s FROM Slot s ORDER BY s.winCount DESC")
    List<Slot> findAllOrderedByWins();
}
