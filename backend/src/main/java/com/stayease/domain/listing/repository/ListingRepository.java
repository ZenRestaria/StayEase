package com.stayease.domain.listing.repository;

import com.stayease.domain.listing.entity.Listing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {

    Optional<Listing> findByPublicId(UUID publicId);
    
    @Query("SELECT l FROM Listing l LEFT JOIN FETCH l.images WHERE l.publicId = :publicId")
    Optional<Listing> findByPublicIdWithImages(@Param("publicId") UUID publicId);
    
    Page<Listing> findByLandlordPublicId(UUID landlordPublicId, Pageable pageable);
    
    Page<Listing> findByCategory(String category, Pageable pageable);
    
    @Query("SELECT l FROM Listing l WHERE LOWER(l.location) LIKE LOWER(CONCAT('%', :location, '%'))")
    Page<Listing> searchByLocation(@Param("location") String location, Pageable pageable);
    
    @Query("SELECT l FROM Listing l WHERE " +
           "(:location IS NULL OR LOWER(l.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:minPrice IS NULL OR l.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR l.price <= :maxPrice) AND " +
           "(:guests IS NULL OR l.guests >= :guests) AND " +
           "(:category IS NULL OR l.category = :category)")
    Page<Listing> searchWithFilters(
        @Param("location") String location,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice,
        @Param("guests") Integer guests,
        @Param("category") String category,
        Pageable pageable
    );
    
    @Query("SELECT l FROM Listing l WHERE l.status = 'ACTIVE' AND l.isActive = true")
    Page<Listing> findAllActiveListings(Pageable pageable);
    
    @Query("SELECT COUNT(l) FROM Listing l WHERE l.landlordPublicId = :landlordPublicId")
    Long countByLandlordPublicId(@Param("landlordPublicId") UUID landlordPublicId);
}