package com.slotmachine.controller;

import com.slotmachine.dto.SlotDto;
import com.slotmachine.request.SlotRequest;
import com.slotmachine.responses.SlotResponse;
import com.slotmachine.service.SlotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@RestController
@RequestMapping("/api/slots")
@CrossOrigin(origins = "*")
public class SlotController {

    private final SlotService slotService;
    private static final String UPLOAD_DIR = "uploads/symbols/";
    private static final List<String> ALLOWED_TYPES =
            List.of("image/jpeg", "image/png", "image/gif", "image/webp");

    public SlotController(SlotService slotService) {
        this.slotService = slotService;
        try { Files.createDirectories(Paths.get(UPLOAD_DIR)); } catch (IOException ignored) {}
    }

    // ── Session endpoints ──────────────────────────────────────────────────

    @PostMapping("/session")
    public ResponseEntity<?> createSession(@RequestParam String playerName,
                                           @RequestParam double initialDeposit) {
        try {
            SlotDto dto = slotService.createSession(playerName.trim(), initialDeposit);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/session/{id}")
    public ResponseEntity<SlotDto> getSession(@PathVariable Long id) {
        return slotService.getSession(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/sessions")
    public ResponseEntity<List<SlotDto>> getAllSessions() {
        return ResponseEntity.ok(slotService.getAllSessions());
    }

    @DeleteMapping("/session/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        slotService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }

    // ── Game endpoint ──────────────────────────────────────────────────────

    @PostMapping("/session/{id}/spin")
    public ResponseEntity<?> spin(@PathVariable Long id,
                                  @RequestBody SlotRequest request) {
        try {
            SlotResponse response = slotService.spin(id, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ── Payment endpoints ──────────────────────────────────────────────────

    @PostMapping("/session/{id}/deposit")
    public ResponseEntity<?> deposit(@PathVariable Long id,
                                     @RequestParam double amount) {
        try {
            SlotDto dto = slotService.deposit(id, amount);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/session/{id}/withdraw")
    public ResponseEntity<?> withdraw(@PathVariable Long id,
                                      @RequestParam double amount) {
        try {
            SlotDto dto = slotService.withdraw(id, amount);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ── Symbol image endpoints ─────────────────────────────────────────────

    @PostMapping("/symbols/upload")
    public ResponseEntity<Map<String, String>> uploadSymbol(
            @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) return ResponseEntity.badRequest().body(Map.of("error", "Empty file"));
            String contentType = file.getContentType();
            if (contentType == null || !ALLOWED_TYPES.contains(contentType))
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid file type"));

            String original = file.getOriginalFilename();
            String ext = (original != null && original.contains("."))
                    ? original.substring(original.lastIndexOf(".")) : "";
            String filename = UUID.randomUUID() + ext;
            Files.copy(file.getInputStream(), Paths.get(UPLOAD_DIR + filename),
                    StandardCopyOption.REPLACE_EXISTING);
            return ResponseEntity.ok(Map.of("url", "/uploads/symbols/" + filename));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Upload failed"));
        }
    }

    @GetMapping("/symbols")
    public ResponseEntity<List<String>> listSymbols() {
        try {
            List<String> urls = new ArrayList<>();
            Path dir = Paths.get(UPLOAD_DIR);
            if (Files.exists(dir)) {
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
                    for (Path p : stream)
                        urls.add("/uploads/symbols/" + p.getFileName());
                }
            }
            return ResponseEntity.ok(urls);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/symbols")
    public ResponseEntity<Void> deleteSymbol(@RequestBody Map<String, String> body) {
        String url = body.get("url");
        if (url == null || url.isBlank()) return ResponseEntity.badRequest().build();
        try {
            String filename = url.replace("/uploads/symbols/", "");
            Files.deleteIfExists(Paths.get(UPLOAD_DIR + filename));
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
