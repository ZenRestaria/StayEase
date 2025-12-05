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
public class CreateListingDTO {
    
    // Basic Information
    @NotBlank(message = "Title is required")
    @Size(min = 10, max = 255, message = "Title must be between 10 and 255 characters")
    private String title;
    
    @NotBlank(message = "Description is required")
    @Size(min = 50, max = 5000, message = "Description must be between 50 and 5000 characters")
    private String description;
    
    @NotBlank(message = "Property type is required")
    private String propertyType;
    
    @NotBlank(message = "Room type is required")
    private String roomType;
    
    // Location
    @NotBlank(message = "Address is required")
    private String address;
    
    @NotBlank(message = "City is required")
    private String city;
    
    private String state;
    
    @NotBlank(message = "Country is required")
    private String country;
    
    private String postalCode;
    
    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    private BigDecimal latitude;
    
    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    private BigDecimal longitude;
    
    // Property Details
    @NotNull(message = "Number of bedrooms is required")
    @Min(value = 0, message = "Bedrooms must be at least 0")
    @Max(value = 50, message = "Bedrooms cannot exceed 50")
    private Integer bedrooms;
    
    @NotNull(message = "Number of beds is required")
    @Min(value = 1, message = "Must have at least 1 bed")
    @Max(value = 100, message = "Beds cannot exceed 100")
    private Integer beds;
    
    @NotNull(message = "Number of bathrooms is required")
    @DecimalMin(value = "0.5", message = "Must have at least 0.5 bathroom")
    @DecimalMax(value = "50.0", message = "Bathrooms cannot exceed 50")
    private BigDecimal bathrooms;
    
    @NotNull(message = "Maximum number of guests is required")
    @Min(value = 1, message = "Must accommodate at least 1 guest")
    @Max(value = 100, message = "Guests cannot exceed 100")
    private Integer maxGuests;
    
    @Min(value = 1, message = "Square feet must be positive")
    private Integer squareFeet;
    
    // Pricing
    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @DecimalMax(value = "100000.00", message = "Price cannot exceed 100,000")
    private BigDecimal basePrice;
    
    @DecimalMin(value = "0.00", message = "Cleaning fee cannot be negative")
    @DecimalMax(value = "10000.00", message = "Cleaning fee cannot exceed 10,000")
    private BigDecimal cleaningFee;
    
    @DecimalMin(value = "0.00", message = "Service fee cannot be negative")
    @DecimalMax(value = "100.00", message = "Service fee cannot exceed 100%")
    private BigDecimal serviceFeePercentage;
    
    @Size(min = 3, max = 3, message = "Currency must be 3 characters (ISO 4217)")
    private String currency;
    
    // Amenities
    private List<String> amenities;
    
    // House Rules
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    
    @Min(value = 1, message = "Minimum nights must be at least 1")
    @Max(value = 365, message = "Minimum nights cannot exceed 365")
    private Integer minNights;
    
    @Min(value = 1, message = "Maximum nights must be at least 1")
    @Max(value = 1095, message = "Maximum nights cannot exceed 1095 (3 years)")
    private Integer maxNights;
    
    private Boolean instantBook;
    
    private String cancellationPolicy;
    
    // Additional House Rules (e.g., {"pets": true, "smoking": false, "parties": false})
    private Map<String, Boolean> houseRules;
    
    // Images (URLs to uploaded images)
    private List<CreateListingImageDTO> images;
}