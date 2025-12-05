package com.stayease.domain.listing.repository;

import com.stayease.domain.listing.entity.FavoriteListing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FavoriteListingRepository extends JpaRepository<FavoriteListing, Long> {

    Optional<FavoriteListing> findByUserPublicIdAndListingPublicId(UUID userPublicId, UUID listingPublicId);
    
    boolean existsByUserPublicIdAndListingPublicId(UUID userPublicId, UUID listingPublicId);
    
    void deleteByUserPublicIdAndListingPublicId(UUID userPublicId, UUID listingPublicId);
    
    Page<FavoriteListing> findByUserPublicId(UUID userPublicId, Pageable pageable);
    
    @Query("SELECT f.listingPublicId FROM FavoriteListing f WHERE f.userPublicId = :userPublicId")
    List<UUID> findListingPublicIdsByUserPublicId(@Param("userPublicId") UUID userPublicId);
    
    long countByListingPublicId(UUID listingPublicId);
    
    long countByUserPublicId(UUID userPublicId);
}