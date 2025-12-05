package com.stayease.domain.listing.dto;

import com.stayease.domain.listing.entity.Listing;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListingDTO {
    
    private UUID id;
    
    // Landlord info
    private LandlordInfo landlord;
    
    // Basic Information
    private String title;
    private String description;
    private String propertyType;
    private String roomType;
    
    // Location
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private BigDecimal latitude;
    private BigDecimal longitude;
    
    // Property Details
    private Integer bedrooms;
    private Integer beds;
    private BigDecimal bathrooms;
    private Integer maxGuests;
    private Integer squareFeet;
    
    // Pricing
    private BigDecimal basePrice;
    private BigDecimal cleaningFee;
    private BigDecimal serviceFeePercentage;
    private String currency;
    
    // Amenities
    private List<String> amenities;
    
    // House Rules
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private Integer minNights;
    private Integer maxNights;
    private Boolean instantBook;
    private String cancellationPolicy;
    private Map<String, Boolean> houseRules;
    
    // Status
    private String status;
    private Boolean isActive;
    
    // Statistics
    private Integer viewCount;
    private Integer bookingCount;
    private BigDecimal averageRating;
    private Integer reviewCount;
    
    // Images
    private List<ListingImageDTO> images;
    
    // Timestamps
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private ZonedDateTime publishedAt;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LandlordInfo {
        private UUID id;
        private String firstName;
        private String lastName;
        private String email;
        private String profileImage;
        private ZonedDateTime joinedDate;
    }
}