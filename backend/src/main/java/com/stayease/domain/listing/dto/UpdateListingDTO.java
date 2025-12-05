package com.stayease.domain.listing.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateListingDTO {
    
    // Basic Information
    @Size(min = 10, max = 255, message = "Title must be between 10 and 255 characters")
    private String title;
    
    @Size(min = 50, max = 5000, message = "Description must be between 50 and 5000 characters")
    private String description;
    
    private String propertyType;
    private String roomType;
    
    // Location
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    
    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    private BigDecimal latitude;
    
    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    private BigDecimal longitude;
    
    // Property Details
    @Min(value = 0)
    @Max(value = 50)
    private Integer bedrooms;
    
    @Min(value = 1)
    @Max(value = 100)
    private Integer beds;
    
    @DecimalMin(value = "0.5")
    @DecimalMax(value = "50.0")
    private BigDecimal bathrooms;
    
    @Min(value = 1)
    @Max(value = 100)
    private Integer maxGuests;
    
    @Min(value = 1)
    private Integer squareFeet;
    
    // Pricing
    @DecimalMin(value = "0.01")
    @DecimalMax(value = "100000.00")
    private BigDecimal basePrice;
    
    @DecimalMin(value = "0.00")
    @DecimalMax(value = "10000.00")
    private BigDecimal cleaningFee;
    
    @DecimalMin(value = "0.00")
    @DecimalMax(value = "100.00")
    private BigDecimal serviceFeePercentage;
    
    // Amenities
    private List<String> amenities;
    
    // House Rules
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    
    @Min(value = 1)
    @Max(value = 365)
    private Integer minNights;
    
    @Min(value = 1)
    @Max(value = 1095)
    private Integer maxNights;
    
    private Boolean instantBook;
    private String cancellationPolicy;
    private Map<String, Boolean> houseRules;
    
    // Status
    private String status;
    private Boolean isActive;
    
    // Images
    private List<ListingImageDTO> images;
}