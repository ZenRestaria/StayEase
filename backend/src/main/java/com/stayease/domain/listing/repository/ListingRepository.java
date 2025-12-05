package com.stayease.domain.listing.repository;

import com.stayease.domain.listing.entity.ListingImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ListingImageRepository extends JpaRepository<ListingImage, UUID> {

    // Find all images for a listing
    List<ListingImage> findByListingIdOrderByDisplayOrderAsc(UUID listingId);

    // Find primary image for a listing
    Optional<ListingImage> findByListingIdAndIsPrimaryTrue(UUID listingId);

    // Delete all images for a listing
    @Modifying
    @Query("DELETE FROM ListingImage li WHERE li.listing.id = :listingId")
    void deleteByListingId(@Param("listingId") UUID listingId);

    // Update display order
    @Modifying
    @Query("UPDATE ListingImage li SET li.displayOrder = :order WHERE li.id = :imageId")
    void updateDisplayOrder(@Param("imageId") UUID imageId, @Param("order") Integer order);

    // Set primary image (unset others first)
    @Modifying
    @Query("UPDATE ListingImage li SET li.isPrimary = false WHERE li.listing.id = :listingId")
    void unsetPrimaryForListing(@Param("listingId") UUID listingId);

    // Count images for a listing
    long countByListingId(UUID listingId);
}