package com.bookstore.web.dto;

import java.util.Map;

import lombok.Data;

@Data
public class RatingStatsDTO {
    private int totalRatings = 0;
    private double averageRating = 0.0;
    private Map<Integer, Long> ratingCounts;
    private Map<Integer, Integer> ratingPercentages; 
}