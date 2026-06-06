package com.example.civicsdg.service;

import org.springframework.stereotype.Service;

@Service
public class IssueService {

    public String mapToSDG(String issueType) {

        if (issueType == null) return "SDG 11 - Sustainable Cities";

        switch (issueType.toLowerCase()) {
            case "garbage":
                return "SDG 11 - Sustainable Cities";
            case "water":
                return "SDG 6 - Clean Water";
            case "road":
                return "SDG 9 - Infrastructure";
            case "electricity":
                return "SDG 7 - Clean Energy";
            default:
                return "SDG 11 - Sustainable Cities";
        }
    }
}
