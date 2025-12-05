package com.stayease.domain.listing.controller;

import com.stayease.domain.listing.dto.*;
import com.stayease.domain.listing.service.ListingService;
import com.stayease.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/listings")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ListingController {

    private final ListingService listingService;

    /**
     * Create a new listing
     * POST /api/listings
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ListingDTO>> createListing(
            @Valid @RequestBody CreateListingDTO createDTO) {
        
        log.info("REST request to create listing: {}", createDTO.getTitle());
        ListingDTO created = listingService.createListing(createDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Listing created successfully"));
    }

    /**
     * Get listing by publicId
     * GET /api/listings/{publicId}
     */
    @GetMapping("/{publicId}")
    public ResponseEntity<ApiResponse<ListingDTO>> getListing(@PathVariable UUID publicId) {
        log.info("REST request to get listing: {}", publicId);
        ListingDTO listing = listingService.getListing(publicId);

        return ResponseEntity.ok(ApiResponse.success(listing));
    }

    /**
     * Update listing
     * PUT /api/listings/{publicId}
     */
    @PutMapping("/{publicId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ListingDTO>> updateListing(
            @PathVariable UUID publicId,
            @Valid @RequestBody UpdateListingDTO updateDTO) {
        
        log.info("REST request to update listing: {}", publicId);
        ListingDTO updated = listingService.updateListing(publicId, updateDTO);

        return ResponseEntity.ok(ApiResponse.success(updated, "Listing updated successfully"));
    }

    /**
     * Delete listing
     * DELETE /api/listings/{publicId}
     */
    @DeleteMapping("/{publicId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> deleteListing(@PathVariable UUID publicId) {
        log.info("REST request to delete listing: {}", publicId);
        listingService.deleteListing(publicId);

        return ResponseEntity.ok(ApiResponse.success(null, "Listing deleted successfully"));
    }

    /**
     * Search listings with filters
     * GET /api/listings/search
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ListingDTO>>> searchListings(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer guests,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("REST request to search listings - location: {}, guests: {}", location, guests);

        SearchListingDTO searchDTO = SearchListingDTO.builder()
                .location(location)
                .guests(guests)
                .category(category)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .sortBy(sortBy)
                .page(page)
                .size(size)
                .build();

        Page<ListingDTO> results = listingService.searchListings(searchDTO);

        return ResponseEntity.ok(ApiResponse.success(results));
    }

    /**
     * Get all listings
     * GET /api/listings
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ListingDTO>>> getAllListings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("REST request to get all listings");
        Page<ListingDTO> results = listingService.getAllListings(page, size);

        return ResponseEntity.ok(ApiResponse.success(results));
    }

    /**
     * Get listings by category
     * GET /api/listings/category/{category}
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<Page<ListingDTO>>> getListingsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("REST request to get listings by category: {}", category);
        Page<ListingDTO> listings = listingService.getListingsByCategory(category, page, size);

        return ResponseEntity.ok(ApiResponse.success(listings));
    }

    /**
     * Get current user's listings
     * GET /api/listings/my-listings
     */
    @GetMapping("/my-listings")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Page<ListingDTO>>> getMyListings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("REST request to get current user's listings");
        Page<ListingDTO> listings = listingService.getMyListings(page, size);

        return ResponseEntity.ok(ApiResponse.success(listings));
    }

    /**
     * Get listings by landlord
     * GET /api/listings/landlord/{landlordPublicId}
     */
    @GetMapping("/landlord/{landlordPublicId}")
    public ResponseEntity<ApiResponse<Page<ListingDTO>>> getListingsByLandlord(
            @PathVariable UUID landlordPublicId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("REST request to get listings by landlord: {}", landlordPublicId);
        Page<ListingDTO> listings = listingService.getListingsByLandlord(landlordPublicId, page, size);

        return ResponseEntity.ok(ApiResponse.success(listings));
    }
}