package com.stayease.domain.listing.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateListingImageDTO {
    
    @NotBlank(message = "Image URL is required")
    private String imageUrl;
    
    private Boolean isPrimary;
    private Integer displayOrder;
    private String caption;
}