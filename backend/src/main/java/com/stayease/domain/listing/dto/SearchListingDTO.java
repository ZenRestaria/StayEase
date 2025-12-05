package com.stayease.domain.listing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchListingDTO {
    
    // Location search
    private String location;  // City or country
    private String city;
    private String country;
    
    // Geographic search
    private Double latitude;
    private Double longitude;
    private Double radiusKm;  // Search radius in kilometers
    
    // Date range (for availability checking)
    private LocalDate checkIn;
    private LocalDate checkOut;
    
    // Guest requirements
    private Integer guests;
    
    // Property filters
    private String propertyType;
    private String roomType;
    
    // Capacity filters
    private Integer minBedrooms;
    private Integer minBeds;
    private BigDecimal minBathrooms;
    
    // Price filters
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    
    // Amenity filters
    private List<String> amenities;
    
    // Other filters
    private Boolean instantBook;
    private BigDecimal minRating;
    
    // Sorting
    private String sortBy;  // price_asc, price_desc, rating, popular, newest
    
    // Pagination
    private Integer page;
    private Integer size;
}