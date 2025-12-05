package com.stayease.domain.listing.entity;

import com.stayease.domain.user.entity.User;
import com.stayease.shared.constant.ListingStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.*;

@Entity
@Table(name = "listing")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "listing_seq")
    @SequenceGenerator(name = "listing_seq", sequenceName = "listing_seq", allocationSize = 50)
    private Long id;

    @Column(name = "public_id", nullable = false, unique = true, updatable = false)
    private UUID publicId;

    @Column(name = "landlord_public_id", nullable = false)
    private UUID landlordPublicId;

    // For eager loading landlord info (optional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "landlord_public_id", referencedColumnName = "public_id", insertable = false, updatable = false)
    private User landlord;

    // Basic Information
    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "property_type", length = 50)
    private String propertyType;

    @Column(name = "room_type", length = 50)
    private String roomType;

    // Location
    @Column(nullable = false, length = 255)
    private String location;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(length = 100)
    private String country;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(precision = 11, scale = 8)
    private BigDecimal longitude;

    // Property Details
    @Column(nullable = false)
    private Integer guests;

    @Column(nullable = false)
    private Integer bedrooms;

    @Column(nullable = false)
    private Integer beds;

    @Column(nullable = false)
    private Integer bathrooms;

    @Column(name = "square_feet")
    private Integer squareFeet;

    // Pricing
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "cleaning_fee", precision = 12, scale = 2)
    private BigDecimal cleaningFee;

    @Column(name = "service_fee_percentage", precision = 5, scale = 2)
    private BigDecimal serviceFeePercentage;

    @Column(nullable = false, length = 10)
    private String currency = "USD";

    // Amenities (stored as JSON string)
    @Column(columnDefinition = "TEXT")
    private String amenities;

    // Category
    @Column(nullable = false, length = 100)
    private String category;

    // House Rules
    @Column(name = "check_in_time")
    private LocalTime checkInTime;

    @Column(name = "check_out_time")
    private LocalTime checkOutTime;

    @Column(name = "min_nights")
    private Integer minNights = 1;

    @Column(name = "max_nights")
    private Integer maxNights = 365;

    @Column(name = "instant_book")
    private Boolean instantBook = false;

    @Column(name = "cancellation_policy", length = 50)
    private String cancellationPolicy;

    @Column(columnDefinition = "TEXT")
    private String rules;

    @Column(name = "house_rules", columnDefinition = "TEXT")
    private String houseRules; // JSON object

    // Status
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private ListingStatus status = ListingStatus.DRAFT;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // Statistics
    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Column(name = "booking_count")
    private Integer bookingCount = 0;

    @Column(name = "favorite_count")
    private Integer favoriteCount = 0;

    @Column(name = "average_rating", precision = 3, scale = 2)
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Column(name = "review_count")
    private Integer reviewCount = 0;

    // Images
    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("displayOrder ASC")
    @Builder.Default
    private List<ListingImage> images = new ArrayList<>();

    // Timestamps
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @Column(name = "published_at")
    private ZonedDateTime publishedAt;

    @PrePersist
    protected void onCreate() {
        if (publicId == null) {
            publicId = UUID.randomUUID();
        }
    }

    // Helper methods
    public void addImage(ListingImage image) {
        images.add(image);
        image.setListing(this);
    }

    public void removeImage(ListingImage image) {
        images.remove(image);
        image.setListing(null);
    }

    public void incrementViewCount() {
        this.viewCount = (this.viewCount == null ? 0 : this.viewCount) + 1;
    }

    public void incrementBookingCount() {
        this.bookingCount = (this.bookingCount == null ? 0 : this.bookingCount) + 1;
    }

    public void incrementFavoriteCount() {
        this.favoriteCount = (this.favoriteCount == null ? 0 : this.favoriteCount) + 1;
    }

    public void decrementFavoriteCount() {
        this.favoriteCount = Math.max(0, (this.favoriteCount == null ? 0 : this.favoriteCount) - 1);
    }

    public void publish() {
        this.status = ListingStatus.ACTIVE;
        this.isActive = true;
        this.publishedAt = ZonedDateTime.now();
    }

    public void unpublish() {
        this.status = ListingStatus.INACTIVE;
        this.isActive = false;
    }
}