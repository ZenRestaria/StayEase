package com.stayease.domain.listing.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "favorite_listing", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_public_id", "listing_public_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteListing {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "favorite_listing_seq")
    @SequenceGenerator(name = "favorite_listing_seq", sequenceName = "favorite_listing_seq", allocationSize = 50)
    private Long id;

    @Column(name = "user_public_id", nullable = false)
    private UUID userPublicId;

    @Column(name = "listing_public_id", nullable = false)
    private UUID listingPublicId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FavoriteListing)) return false;
        FavoriteListing that = (FavoriteListing) o;
        return userPublicId != null && listingPublicId != null &&
               userPublicId.equals(that.userPublicId) && 
               listingPublicId.equals(that.listingPublicId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}