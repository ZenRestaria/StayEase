package com.stayease.shared.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stayease.domain.listing.dto.*;
import com.stayease.domain.listing.entity.Listing;
import com.stayease.domain.listing.entity.ListingImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ListingMapper {

    private final ObjectMapper objectMapper;

    public ListingDTO toDTO(Listing listing) {
        if (listing == null) {
            return null;
        }

        return ListingDTO.builder()
                .id(listing.getPublicId())
                .landlord(toLandlordInfo(listing))
                .title(listing.getTitle())
                .description(listing.getDescription())
                .propertyType(listing.getPropertyType())
                .roomType(listing.getRoomType())
                .address(listing.getAddress())
                .city(listing.getCity())
                .state(listing.getState())
                .country(listing.getCountry())
                .postalCode(listing.getPostalCode())
                .latitude(listing.getLatitude())
                .longitude(listing.getLongitude())
                .bedrooms(listing.getBedrooms())
                .beds(listing.getBeds())
                .bathrooms(BigDecimal.valueOf(listing.getBathrooms()))
                .maxGuests(listing.getGuests())
                .squareFeet(listing.getSquareFeet())
                .basePrice(listing.getPrice())
                .cleaningFee(listing.getCleaningFee())
                .serviceFeePercentage(listing.getServiceFeePercentage())
                .currency(listing.getCurrency())
                .amenities(parseAmenities(listing.getAmenities()))
                .checkInTime(listing.getCheckInTime())
                .checkOutTime(listing.getCheckOutTime())
                .minNights(listing.getMinNights())
                .maxNights(listing.getMaxNights())
                .instantBook(listing.getInstantBook())
                .cancellationPolicy(listing.getCancellationPolicy())
                .houseRules(parseHouseRules(listing.getHouseRules()))
                .status(listing.getStatus() != null ? listing.getStatus().name() : null)
                .isActive(listing.getIsActive())
                .viewCount(listing.getViewCount())
                .bookingCount(listing.getBookingCount())
                .averageRating(listing.getAverageRating())
                .reviewCount(listing.getReviewCount())
                .images(listing.getImages().stream()
                        .map(this::toImageDTO)
                        .collect(Collectors.toList()))
                .createdAt(listing.getCreatedAt())
                .updatedAt(listing.getUpdatedAt())
                .publishedAt(listing.getPublishedAt())
                .build();
    }

    public Listing toEntity(CreateListingDTO dto) {
        if (dto == null) {
            return null;
        }

        return Listing.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .propertyType(dto.getPropertyType())
                .roomType(dto.getRoomType())
                .location(buildLocationString(dto))
                .address(dto.getAddress())
                .city(dto.getCity())
                .state(dto.getState())
                .country(dto.getCountry())
                .postalCode(dto.getPostalCode())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .guests(dto.getMaxGuests())
                .bedrooms(dto.getBedrooms())
                .beds(dto.getBeds())
                .bathrooms(dto.getBathrooms() != null ? dto.getBathrooms().intValue() : 1)
                .squareFeet(dto.getSquareFeet())
                .price(dto.getBasePrice())
                .cleaningFee(dto.getCleaningFee())
                .serviceFeePercentage(dto.getServiceFeePercentage())
                .currency(dto.getCurrency() != null ? dto.getCurrency() : "USD")
                .amenities(serializeAmenities(dto.getAmenities()))
                .category(dto.getPropertyType())
                .checkInTime(dto.getCheckInTime())
                .checkOutTime(dto.getCheckOutTime())
                .minNights(dto.getMinNights())
                .maxNights(dto.getMaxNights())
                .instantBook(dto.getInstantBook())
                .cancellationPolicy(dto.getCancellationPolicy())
                .houseRules(serializeHouseRules(dto.getHouseRules()))
                .build();
    }

    public void updateEntityFromDTO(UpdateListingDTO dto, Listing listing) {
        if (dto == null || listing == null) {
            return;
        }

        if (dto.getTitle() != null) {
            listing.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            listing.setDescription(dto.getDescription());
        }
        if (dto.getPropertyType() != null) {
            listing.setPropertyType(dto.getPropertyType());
            listing.setCategory(dto.getPropertyType());
        }
        if (dto.getRoomType() != null) {
            listing.setRoomType(dto.getRoomType());
        }
        if (dto.getAddress() != null) {
            listing.setAddress(dto.getAddress());
        }
        if (dto.getCity() != null) {
            listing.setCity(dto.getCity());
        }
        if (dto.getState() != null) {
            listing.setState(dto.getState());
        }
        if (dto.getCountry() != null) {
            listing.setCountry(dto.getCountry());
        }
        if (dto.getPostalCode() != null) {
            listing.setPostalCode(dto.getPostalCode());
        }
        if (dto.getLatitude() != null) {
            listing.setLatitude(dto.getLatitude());
        }
        if (dto.getLongitude() != null) {
            listing.setLongitude(dto.getLongitude());
        }
        if (dto.getBedrooms() != null) {
            listing.setBedrooms(dto.getBedrooms());
        }
        if (dto.getBeds() != null) {
            listing.setBeds(dto.getBeds());
        }
        if (dto.getBathrooms() != null) {
            listing.setBathrooms(dto.getBathrooms().intValue());
        }
        if (dto.getMaxGuests() != null) {
            listing.setGuests(dto.getMaxGuests());
        }
        if (dto.getSquareFeet() != null) {
            listing.setSquareFeet(dto.getSquareFeet());
        }
        if (dto.getBasePrice() != null) {
            listing.setPrice(dto.getBasePrice());
        }
        if (dto.getCleaningFee() != null) {
            listing.setCleaningFee(dto.getCleaningFee());
        }
        if (dto.getServiceFeePercentage() != null) {
            listing.setServiceFeePercentage(dto.getServiceFeePercentage());
        }
        if (dto.getAmenities() != null) {
            listing.setAmenities(serializeAmenities(dto.getAmenities()));
        }
        if (dto.getCheckInTime() != null) {
            listing.setCheckInTime(dto.getCheckInTime());
        }
        if (dto.getCheckOutTime() != null) {
            listing.setCheckOutTime(dto.getCheckOutTime());
        }
        if (dto.getMinNights() != null) {
            listing.setMinNights(dto.getMinNights());
        }
        if (dto.getMaxNights() != null) {
            listing.setMaxNights(dto.getMaxNights());
        }
        if (dto.getInstantBook() != null) {
            listing.setInstantBook(dto.getInstantBook());
        }
        if (dto.getCancellationPolicy() != null) {
            listing.setCancellationPolicy(dto.getCancellationPolicy());
        }
        if (dto.getHouseRules() != null) {
            listing.setHouseRules(serializeHouseRules(dto.getHouseRules()));
        }
        if (dto.getIsActive() != null) {
            listing.setIsActive(dto.getIsActive());
        }
        
        // Update location string
        listing.setLocation(buildLocationFromListing(listing));
    }

    public ListingImageDTO toImageDTO(ListingImage image) {
        if (image == null) {
            return null;
        }

        return ListingImageDTO.builder()
                .id(image.getPublicId())
                .imageUrl(image.getUrl())
                .caption(image.getCaption())
                .displayOrder(image.getDisplayOrder())
                .isPrimary(image.getIsPrimary())
                .createdAt(image.getCreatedAt())
                .build();
    }

    public ListingImage toImageEntity(CreateListingImageDTO dto) {
        if (dto == null) {
            return null;
        }

        return ListingImage.builder()
                .url(dto.getImageUrl())
                .caption(dto.getCaption())
                .displayOrder(dto.getDisplayOrder() != null ? dto.getDisplayOrder() : 0)
                .isPrimary(dto.getIsPrimary() != null ? dto.getIsPrimary() : false)
                .build();
    }

    private ListingDTO.LandlordInfo toLandlordInfo(Listing listing) {
        if (listing.getLandlord() == null) {
            return null;
        }

        var landlord = listing.getLandlord();
        return ListingDTO.LandlordInfo.builder()
                .id(landlord.getPublicId())
                .firstName(landlord.getFirstName())
                .lastName(landlord.getLastName())
                .email(landlord.getEmail())
                .profileImage(landlord.getImageUrl())
                .joinedDate(toZonedDateTime(landlord.getCreatedAt()))
                .build();
    }

    private String buildLocationString(CreateListingDTO dto) {
        StringBuilder location = new StringBuilder();
        if (dto.getCity() != null) {
            location.append(dto.getCity());
        }
        if (dto.getState() != null) {
            if (location.length() > 0) location.append(", ");
            location.append(dto.getState());
        }
        if (dto.getCountry() != null) {
            if (location.length() > 0) location.append(", ");
            location.append(dto.getCountry());
        }
        return location.toString();
    }

    private String buildLocationFromListing(Listing listing) {
        StringBuilder location = new StringBuilder();
        if (listing.getCity() != null) {
            location.append(listing.getCity());
        }
        if (listing.getState() != null) {
            if (location.length() > 0) location.append(", ");
            location.append(listing.getState());
        }
        if (listing.getCountry() != null) {
            if (location.length() > 0) location.append(", ");
            location.append(listing.getCountry());
        }
        return location.toString();
    }

    private List<String> parseAmenities(String amenitiesJson) {
        if (amenitiesJson == null || amenitiesJson.isBlank()) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(amenitiesJson, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            log.error("Error parsing amenities JSON", e);
            return Collections.emptyList();
        }
    }

    private String serializeAmenities(List<String> amenities) {
        if (amenities == null || amenities.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(amenities);
        } catch (JsonProcessingException e) {
            log.error("Error serializing amenities", e);
            return null;
        }
    }

    private Map<String, Boolean> parseHouseRules(String houseRulesJson) {
        if (houseRulesJson == null || houseRulesJson.isBlank()) {
            return Collections.emptyMap();
        }
        try {
            return objectMapper.readValue(houseRulesJson, new TypeReference<Map<String, Boolean>>() {});
        } catch (JsonProcessingException e) {
            log.error("Error parsing house rules JSON", e);
            return Collections.emptyMap();
        }
    }

    private String serializeHouseRules(Map<String, Boolean> houseRules) {
        if (houseRules == null || houseRules.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(houseRules);
        } catch (JsonProcessingException e) {
            log.error("Error serializing house rules", e);
            return null;
        }
    }

    // Helper method to convert Instant to ZonedDateTime
    private ZonedDateTime toZonedDateTime(Instant instant) {
        return instant != null ? instant.atZone(ZoneId.systemDefault()) : null;
    }
}