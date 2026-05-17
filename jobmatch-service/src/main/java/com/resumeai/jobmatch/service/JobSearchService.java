package com.resumeai.jobmatch.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resumeai.jobmatch.dto.JobSearchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobSearchService {

    private final RestTemplate rest = new RestTemplate();
    private final ObjectMapper om = new ObjectMapper();

    @Value("${jsearch.api-key:}")
    private String jsearchKey;

    @Value("${jsearch.host:jsearch.p.rapidapi.com}")
    private String jsearchHost;

    public List<JobSearchResult> search(String query, String location, int page) {
        if (query == null || query.isBlank()) query = "software engineer";
        if (page < 1) page = 1;

        // Primary: JSearch (real LinkedIn / Indeed / Glassdoor data) if key configured
        if (jsearchKey != null && !jsearchKey.isBlank()) {
            try {
                return searchJSearch(query, location, page);
            } catch (Exception e) {
                log.warn("JSearch failed, falling back to Remotive: {}", e.getMessage());
            }
        }
        // Fallback: Remotive (no API key required)
        try {
            return searchRemotive(query);
        } catch (Exception e) {
            log.error("Remotive failed", e);
            return List.of();
        }
    }

    // --- JSearch (RapidAPI) ---
    private List<JobSearchResult> searchJSearch(String query, String location, int page) throws Exception {
        String q = location != null && !location.isBlank() ? query + " in " + location : query;
        URI uri = UriComponentsBuilder.fromHttpUrl("https://" + jsearchHost + "/search")
                .queryParam("query", q)
                .queryParam("page", page)
                .queryParam("num_pages", 1)
                .build().encode().toUri();

        HttpHeaders h = new HttpHeaders();
        h.set("X-RapidAPI-Key", jsearchKey);
        h.set("X-RapidAPI-Host", jsearchHost);

        ResponseEntity<String> resp = rest.exchange(uri, HttpMethod.GET, new HttpEntity<>(h), String.class);
        JsonNode root = om.readTree(resp.getBody());
        JsonNode data = root.path("data");

        List<JobSearchResult> out = new ArrayList<>();
        for (JsonNode j : data) {
            String loc = String.join(", ",
                    cleanList(j.path("job_city").asText(""), j.path("job_country").asText("")));
            out.add(JobSearchResult.builder()
                    .externalId(j.path("job_id").asText())
                    .title(j.path("job_title").asText())
                    .company(j.path("employer_name").asText())
                    .location(loc.isBlank() ? "Remote" : loc)
                    .description(j.path("job_description").asText(""))
                    .applyUrl(j.path("job_apply_link").asText())
                    .employmentType(j.path("job_employment_type").asText(""))
                    .postedAt(j.path("job_posted_at_datetime_utc").asText(""))
                    .companyLogo(j.path("employer_logo").asText(""))
                    .salary(formatSalary(j))
                    .source("jsearch")
                    .build());
        }
        return out;
    }

    private String formatSalary(JsonNode j) {
        String min = j.path("job_min_salary").asText("");
        String max = j.path("job_max_salary").asText("");
        String cur = j.path("job_salary_currency").asText("");
        if (min.isBlank() && max.isBlank()) return "";
        return (cur + " " + min + " - " + max).trim();
    }

    private List<String> cleanList(String... s) {
        List<String> out = new ArrayList<>();
        for (String x : s) if (x != null && !x.isBlank() && !"null".equals(x)) out.add(x);
        return out;
    }

    // --- Remotive (no key, free) ---
    private List<JobSearchResult> searchRemotive(String query) throws Exception {
        URI uri = UriComponentsBuilder.fromHttpUrl("https://remotive.com/api/remote-jobs")
                .queryParam("search", query)
                .queryParam("limit", 30)
                .build().encode().toUri();

        ResponseEntity<String> resp = rest.getForEntity(uri, String.class);
        JsonNode jobs = om.readTree(resp.getBody()).path("jobs");
        List<JobSearchResult> out = new ArrayList<>();
        for (JsonNode j : jobs) {
            out.add(JobSearchResult.builder()
                    .externalId(j.path("id").asText())
                    .title(j.path("title").asText())
                    .company(j.path("company_name").asText())
                    .location(j.path("candidate_required_location").asText("Remote"))
                    .description(j.path("description").asText(""))
                    .applyUrl(j.path("url").asText())
                    .employmentType(j.path("job_type").asText(""))
                    .postedAt(j.path("publication_date").asText(""))
                    .companyLogo(j.path("company_logo").asText(""))
                    .salary(j.path("salary").asText(""))
                    .source("remotive")
                    .build());
        }
        return out;
    }
}
