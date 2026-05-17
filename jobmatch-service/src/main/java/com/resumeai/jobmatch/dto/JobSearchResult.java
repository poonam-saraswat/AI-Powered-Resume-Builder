package com.resumeai.jobmatch.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class JobSearchResult {
    private String externalId;
    private String title;
    private String company;
    private String location;
    private String description;
    private String applyUrl;
    private String employmentType;
    private String postedAt;
    private String source;
    private String companyLogo;
    private String salary;
}
