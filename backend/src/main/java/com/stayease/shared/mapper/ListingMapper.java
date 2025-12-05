package com.stayease.shared.mapper;

import com.stayease.domain.listing.dto.*;
import com.stayease.domain.listing.entity.Listing;
import com.stayease.domain.listing.entity.ListingImage;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ListingMapper {

    public ListingDTO toDTO(Listing listing) {
        if (listing == null) {
            return null;
        }

        return ListingDTO.builder()
                .publicId(listing.getPublicId())
                .landlord(toLandlordInfo(listing))
                .title(listing.getTitle())
                .description(listing.getDescription())
                .location(listing.getLocation())
                .price(listing.getPrice())
                .currency(listing.getCurrency())
                .guests(listing.getGuests())
                .bedrooms(listing.getBedrooms())
                .beds(listing.getBeds())
                .bathrooms(listing.getBathrooms())
                .category(listing.getCategory())
                .rules(listing.getRules())
                .images(listing.getImages().stream()
                        .map(this::toImageDTO)
                        .collect(Collectors.toList()))
                .createdAt(listing.getCreatedAt())
                .updatedAt(listing.getUpdatedAt())
                .build();
    }

    public Listing toEntity(CreateListingDTO dto) {
        if (dto == null) {
            return null;
        }

        return Listing.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .location(dto.getLocation())
                .price(dto.getPrice())
                .currency(dto.getCurrency() != null ? dto.getCurrency() : "USD")
                .guests(dto.getGuests())
                .bedrooms(dto.getBedrooms())
                .beds(dto.getBeds())
                .bathrooms(dto.getBathrooms())
                .category(dto.getCategory())
                .rules(dto.getRules())
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
        if (dto.getLocation() != null) {
            listing.setLocation(dto.getLocation());
        }
        if (dto.getPrice() != null) {
            listing.setPrice(dto.getPrice());
        }
        if (dto.getGuests() != null) {
            listing.setGuests(dto.getGuests());
        }
        if (dto.getBedrooms() != null) {
            listing.setBedrooms(dto.getBedrooms());
        }
        if (dto.getBeds() != null) {
            listing.setBeds(dto.getBeds());
        }
        if (dto.getBathrooms() != null) {
            listing.setBathrooms(dto.getBathrooms());
        }
        if (dto.getCategory() != null) {
            listing.setCategory(dto.getCategory());
        }
        if (dto.getRules() != null) {
            listing.setRules(dto.getRules());
        }
    }

    public ListingImageDTO toImageDTO(ListingImage image) {
        if (image == null) {
            return null;
        }

        return ListingImageDTO.builder()
                .id(image.getId())
                .url(image.getUrl())
                .caption(image.getCaption())
                .sortOrder(image.getSortOrder())
                .createdAt(image.getCreatedAt())
                .build();
    }

    public ListingImage toImageEntity(CreateListingImageDTO dto) {
        if (dto == null) {
            return null;
        }

        return ListingImage.builder()
                .url(dto.getUrl())
                .caption(dto.getCaption())
                .sortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0)
                .build();
    }

    private ListingDTO.LandlordInfo toLandlordInfo(Listing listing) {
        if (listing.getLandlord() == null) {
            return null;
        }

        var landlord = listing.getLandlord();
        return ListingDTO.LandlordInfo.builder()
                .publicId(landlord.getPublicId())
                .firstName(landlord.getFirstName())
                .lastName(landlord.getLastName())
                .email(landlord.getEmail())
                .imageUrl(landlord.getImageUrl())
                .build();
    }
}