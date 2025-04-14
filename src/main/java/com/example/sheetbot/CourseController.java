package com.example.sheetbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CourseController {

    @Value("${sheet.csv.url}")
    private String csvUrl;

    @GetMapping("/search")
    public List<String> searchCourses(@RequestParam String query) throws Exception {
        URL url = new URL(csvUrl);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            return reader.lines()
                    .filter(line -> line.toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
        }
    }
}
