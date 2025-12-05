package com.stayease.domain.listing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListingImageDTO {
    
    private UUID id;
    private String imageUrl;
    private String url; // Added for compatibility
    private Boolean isPrimary;
    private Integer displayOrder;
    private String caption;
    private ZonedDateTime createdAt;
    
    // Getter methods for both imageUrl and url to support both field names
    public String getImageUrl() {
        return imageUrl != null ? imageUrl : url;
    }
    
    public String getUrl() {
        return url != null ? url : imageUrl;
    }
}