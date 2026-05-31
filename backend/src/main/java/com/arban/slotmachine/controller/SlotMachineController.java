package com.arban.slotmachine.controller;

import com.arban.slotmachine.dto.SpinRequestDTO;
import com.arban.slotmachine.dto.SpinResponseDTO;
import com.arban.slotmachine.entity.GameSession;
import com.arban.slotmachine.service.SlotMachineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for slot machine operations.
 * Thin controller — delegates all logic to service layer.
 */
@RestController
@RequestMapping("/api/slot")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
public class SlotMachineController {

    private final SlotMachineService slotMachineService;

    @PostMapping("/spin")
    public ResponseEntity<SpinResponseDTO> spin(@Valid @RequestBody SpinRequestDTO request) {
        return ResponseEntity.ok(slotMachineService.spin(request));
    }

    @GetMapping("/session/{playerName}")
    public ResponseEntity<GameSession> getSession(@PathVariable String playerName) {
        return ResponseEntity.ok(slotMachineService.getOrCreateSession(playerName));
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<GameSession>> getLeaderboard() {
        return ResponseEntity.ok(slotMachineService.getLeaderboard());
    }

    @PostMapping("/reset/{playerName}")
    public ResponseEntity<GameSession> resetSession(@PathVariable String playerName) {
        return ResponseEntity.ok(slotMachineService.resetSession(playerName));
    }
}
