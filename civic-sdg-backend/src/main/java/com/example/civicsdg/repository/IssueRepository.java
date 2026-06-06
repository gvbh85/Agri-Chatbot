package com.example.civicsdg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.civicsdg.model.Issue;

public interface IssueRepository extends JpaRepository<Issue, Long> {
}
