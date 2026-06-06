package com.example.civicsdg.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.civicsdg.model.Issue;
import com.example.civicsdg.repository.IssueRepository;
import com.example.civicsdg.security.JwtUtil;
import com.example.civicsdg.service.IssueService;

@RestController
@RequestMapping("/api/issues")
@CrossOrigin(origins = "*")
public class IssueController {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private IssueService issueService;

    @Autowired
    private JwtUtil jwtUtil;

    // ================= REPORT ISSUE =================
    @PostMapping
    public ResponseEntity<?> reportIssue(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Issue issue) {

        String email = validateTokenAndGetEmail(authHeader);
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or missing token");
        }

        issue.setEmail(email);
        issue.setSdg(issueService.mapToSDG(issue.getIssueType()));
        issue.setStatus("Pending");

        Issue savedIssue = issueRepository.save(issue);

        return ResponseEntity.ok(savedIssue);
    }

    // ================= GET ALL ISSUES =================
    @GetMapping
    public ResponseEntity<?> getAllIssues(
            @RequestHeader("Authorization") String authHeader) {

        if (!isTokenValid(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or missing token");
        }

        return ResponseEntity.ok(issueRepository.findAll());
    }

    // ================= UPDATE STATUS =================
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStatus(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @RequestBody Issue updated) {

        if (!isTokenValid(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or missing token");
        }

        Optional<Issue> optionalIssue = issueRepository.findById(id);

        if (optionalIssue.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Issue not found");
        }

        Issue issue = optionalIssue.get();
        issue.setStatus(updated.getStatus());

        issueRepository.save(issue);

        return ResponseEntity.ok(issue);
    }

    // ================= DELETE SINGLE ISSUE (DEBUG VERSION) =================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteIssue(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {

        System.out.println("DELETE called for ID: " + id);

        if (!isTokenValid(authHeader)) {
            System.out.println("Token invalid");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or missing token");
        }

        try {

            Optional<Issue> issueOptional = issueRepository.findById(id);

            if (issueOptional.isEmpty()) {
                System.out.println("Issue not found in DB");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Issue not found");
            }

            issueRepository.delete(issueOptional.get());

            System.out.println("Issue deleted successfully");

            return ResponseEntity.ok("Issue deleted successfully");

        } catch (Exception e) {
            e.printStackTrace();   // 🔥 VERY IMPORTANT
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Delete failed: " + e.getMessage());
        }
    }

    // ================= DELETE RESOLVED ISSUES =================
    @DeleteMapping("/resolved")
    public ResponseEntity<?> deleteResolvedIssues(
            @RequestHeader("Authorization") String authHeader) {

        if (!isTokenValid(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or missing token");
        }

        List<Issue> resolvedIssues = issueRepository.findAll()
                .stream()
                .filter(issue ->
                        "Resolved".equalsIgnoreCase(issue.getStatus()))
                .toList();

        if (resolvedIssues.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No resolved issues found");
        }

        issueRepository.deleteAll(resolvedIssues);

        return ResponseEntity.ok("All resolved issues deleted successfully");
    }

    // ================= HELPER METHODS =================

    private boolean isTokenValid(String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }

        String token = authHeader.substring(7);
        return jwtUtil.validateToken(token);
    }

    private String validateTokenAndGetEmail(String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.validateToken(token)) {
            return null;
        }

        return jwtUtil.extractEmail(token);
    }
}
