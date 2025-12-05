package com.stayease.domain.listing.service;

import com.stayease.domain.listing.dto.*;
import com.stayease.domain.listing.entity.Listing;
import com.stayease.domain.listing.entity.ListingImage;
import com.stayease.domain.listing.repository.ListingImageRepository;
import com.stayease.domain.listing.repository.ListingRepository;
import com.stayease.exception.ForbiddenException;
import com.stayease.exception.NotFoundException;
import com.stayease.security.SecurityUtils;
import com.stayease.shared.mapper.ListingMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ListingService {

    private final ListingRepository listingRepository;
    private final ListingImageRepository listingImageRepository;
    private final ListingMapper listingMapper;
    private final SecurityUtils securityUtils;

    /**
     * Create a new listing
     */
    public ListingDTO createListing(CreateListingDTO createDTO) {
        log.info("Creating new listing: {}", createDTO.getTitle());

        // Get current user's publicId
        UUID currentUserPublicId = securityUtils.getCurrentUserPublicId();

        // Convert DTO to Entity
        Listing listing = listingMapper.toEntity(createDTO);
        listing.setLandlordPublicId(currentUserPublicId);

        // Save listing first
        Listing savedListing = listingRepository.save(listing);

        // Add images if provided
        if (createDTO.getImages() != null && !createDTO.getImages().isEmpty()) {
            int order = 0;
            for (CreateListingImageDTO imageDTO : createDTO.getImages()) {
                ListingImage image = listingMapper.toImageEntity(imageDTO);
                image.setListing(savedListing);
                image.setSortOrder(order++);
                savedListing.addImage(image);
            }
            savedListing = listingRepository.save(savedListing);
        }

        log.info("Listing created successfully with publicId: {}", savedListing.getPublicId());
        return listingMapper.toDTO(savedListing);
    }

    /**
     * Get listing by publicId
     */
    @Transactional(readOnly = true)
    public ListingDTO getListing(UUID publicId) {
        log.info("Fetching listing with publicId: {}", publicId);

        Listing listing = listingRepository.findByPublicIdWithImages(publicId)
                .orElseThrow(() -> new NotFoundException("Listing not found with ID: " + publicId));

        return listingMapper.toDTO(listing);
    }

    /**
     * Update existing listing
     */
    public ListingDTO updateListing(UUID publicId, UpdateListingDTO updateDTO) {
        log.info("Updating listing with publicId: {}", publicId);

        Listing listing = listingRepository.findByPublicId(publicId)
                .orElseThrow(() -> new NotFoundException("Listing not found"));

        // Check ownership
        UUID currentUserPublicId = securityUtils.getCurrentUserPublicId();
        if (!listing.getLandlordPublicId().equals(currentUserPublicId)) {
            throw new ForbiddenException("You don't have permission to update this listing");
        }

        // Update listing fields
        listingMapper.updateEntityFromDTO(updateDTO, listing);

        // Update images if provided
        if (updateDTO.getImages() != null) {
            listing.getImages().clear();
            listingImageRepository.flush();

            int order = 0;
            for (ListingImageDTO imageDTO : updateDTO.getImages()) {
                ListingImage image = ListingImage.builder()
                        .url(imageDTO.getUrl())
                        .caption(imageDTO.getCaption())
                        .sortOrder(order++)
                        .build();
                listing.addImage(image);
            }
        }

        Listing updatedListing = listingRepository.save(listing);
        log.info("Listing updated successfully: {}", publicId);

        return listingMapper.toDTO(updatedListing);
    }

    /**
     * Delete listing
     */
    public void deleteListing(UUID publicId) {
        log.info("Deleting listing with publicId: {}", publicId);

        Listing listing = listingRepository.findByPublicId(publicId)
                .orElseThrow(() -> new NotFoundException("Listing not found"));

        // Check ownership
        UUID currentUserPublicId = securityUtils.getCurrentUserPublicId();
        if (!listing.getLandlordPublicId().equals(currentUserPublicId)) {
            throw new ForbiddenException("You don't have permission to delete this listing");
        }

        listingRepository.delete(listing);
        log.info("Listing deleted successfully: {}", publicId);
    }

    /**
     * Search listings with filters
     */
    @Transactional(readOnly = true)
    public Page<ListingDTO> searchListings(SearchListingDTO searchDTO) {
        log.info("Searching listings with filters: {}", searchDTO);

        Pageable pageable = createPageable(searchDTO);
        Page<Listing> listingsPage;

        // If location is provided
        if (searchDTO.getLocation() != null && !searchDTO.getLocation().isBlank()) {
            listingsPage = listingRepository.searchByLocation(
                    searchDTO.getLocation().trim(),
                    pageable
            );
        }
        // Advanced filter search
        else if (hasAdvancedFilters(searchDTO)) {
            listingsPage = listingRepository.searchWithFilters(
                    searchDTO.getLocation(),
                    searchDTO.getMinPrice(),
                    searchDTO.getMaxPrice(),
                    searchDTO.getGuests(),
                    searchDTO.getCategory(),
                    pageable
            );
        }
        // Default: get all listings
        else {
            listingsPage = listingRepository.findAll(pageable);
        }

        return listingsPage.map(listingMapper::toDTO);
    }

    /**
     * Get all listings
     */
    @Transactional(readOnly = true)
    public Page<ListingDTO> getAllListings(int page, int size) {
        log.info("Fetching all listings");

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Listing> listingsPage = listingRepository.findAll(pageable);

        return listingsPage.map(listingMapper::toDTO);
    }

    /**
     * Get listings by category
     */
    @Transactional(readOnly = true)
    public Page<ListingDTO> getListingsByCategory(String category, int page, int size) {
        log.info("Fetching listings by category: {}", category);

        Pageable pageable = PageRequest.of(page, size);
        Page<Listing> listingsPage = listingRepository.findByCategory(category, pageable);

        return listingsPage.map(listingMapper::toDTO);
    }

    /**
     * Get current user's listings
     */
    @Transactional(readOnly = true)
    public Page<ListingDTO> getMyListings(int page, int size) {
        log.info("Fetching current user's listings");

        UUID currentUserPublicId = securityUtils.getCurrentUserPublicId();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Listing> listingsPage = listingRepository.findByLandlordPublicId(currentUserPublicId, pageable);

        return listingsPage.map(listingMapper::toDTO);
    }

    /**
     * Get listings by landlord publicId
     */
    @Transactional(readOnly = true)
    public Page<ListingDTO> getListingsByLandlord(UUID landlordPublicId, int page, int size) {
        log.info("Fetching listings for landlord: {}", landlordPublicId);

        Pageable pageable = PageRequest.of(page, size);
        Page<Listing> listingsPage = listingRepository.findByLandlordPublicId(landlordPublicId, pageable);

        return listingsPage.map(listingMapper::toDTO);
    }

    // Helper methods

    private Pageable createPageable(SearchListingDTO searchDTO) {
        int page = searchDTO.getPage() != null ? searchDTO.getPage() : 0;
        int size = searchDTO.getSize() != null ? searchDTO.getSize() : 20;

        Sort sort = Sort.by("createdAt").descending();

        if (searchDTO.getSortBy() != null) {
            switch (searchDTO.getSortBy().toLowerCase()) {
                case "price_asc":
                    sort = Sort.by("price").ascending();
                    break;
                case "price_desc":
                    sort = Sort.by("price").descending();
                    break;
                case "newest":
                    sort = Sort.by("createdAt").descending();
                    break;
            }
        }

        return PageRequest.of(page, size, sort);
    }

    private boolean hasAdvancedFilters(SearchListingDTO searchDTO) {
        return searchDTO.getLocation() != null ||
               searchDTO.getMinPrice() != null ||
               searchDTO.getMaxPrice() != null ||
               searchDTO.getGuests() != null ||
               searchDTO.getCategory() != null;
    }
}