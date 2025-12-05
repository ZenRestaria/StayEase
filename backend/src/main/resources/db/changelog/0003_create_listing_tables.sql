--liquibase formatted sql

--changeset stayease:0003-create-listing-table
CREATE TABLE listing (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL UNIQUE,
    landlord_public_id UUID NOT NULL,
    
    -- Basic Information
    title VARCHAR(255) NOT NULL,
    description TEXT,
    property_type VARCHAR(50),
    room_type VARCHAR(50),
    
    -- Location
    location VARCHAR(255) NOT NULL,
    address TEXT,
    city VARCHAR(100),
    state VARCHAR(100),
    country VARCHAR(100),
    postal_code VARCHAR(20),
    latitude DECIMAL(10,8),
    longitude DECIMAL(11,8),
    
    -- Property Details
    guests INT NOT NULL,
    bedrooms INT NOT NULL,
    beds INT NOT NULL,
    bathrooms INT NOT NULL,
    square_feet INT,
    
    -- Pricing
    price DECIMAL(12,2) NOT NULL,
    cleaning_fee DECIMAL(12,2),
    service_fee_percentage DECIMAL(5,2),
    currency VARCHAR(10) NOT NULL DEFAULT 'USD',
    
    -- Amenities (JSON)
    amenities TEXT,
    
    -- Category
    category VARCHAR(100) NOT NULL,
    
    -- House Rules
    check_in_time TIME,
    check_out_time TIME,
    min_nights INT DEFAULT 1,
    max_nights INT DEFAULT 365,
    instant_book BOOLEAN DEFAULT FALSE,
    cancellation_policy VARCHAR(50),
    rules TEXT,
    house_rules TEXT,
    
    -- Status
    status VARCHAR(50) DEFAULT 'DRAFT',
    is_active BOOLEAN DEFAULT TRUE,
    
    -- Statistics
    view_count INT DEFAULT 0,
    booking_count INT DEFAULT 0,
    favorite_count INT DEFAULT 0,
    average_rating DECIMAL(3,2) DEFAULT 0,
    review_count INT DEFAULT 0,
    
    -- Timestamps
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    published_at TIMESTAMP,
    
    CONSTRAINT fk_listing_landlord FOREIGN KEY (landlord_public_id) REFERENCES "user"(public_id) ON DELETE CASCADE
);
--rollback DROP TABLE listing;

--changeset stayease:0003-create-listing-image-table
CREATE TABLE listing_image (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL UNIQUE,
    listing_id BIGINT NOT NULL,
    url VARCHAR(1000) NOT NULL,
    caption VARCHAR(255),
    display_order INT DEFAULT 0,
    is_primary BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_listing_image_listing FOREIGN KEY (listing_id) REFERENCES listing(id) ON DELETE CASCADE
);
--rollback DROP TABLE listing_image;

--changeset stayease:0003-create-favorite-listing-table
CREATE TABLE favorite_listing (
    id BIGSERIAL PRIMARY KEY,
    user_public_id UUID NOT NULL,
    listing_public_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_favorite_user FOREIGN KEY (user_public_id) REFERENCES "user"(public_id) ON DELETE CASCADE,
    CONSTRAINT fk_favorite_listing FOREIGN KEY (listing_public_id) REFERENCES listing(public_id) ON DELETE CASCADE,
    CONSTRAINT uk_favorite_user_listing UNIQUE (user_public_id, listing_public_id)
);
--rollback DROP TABLE favorite_listing;

--changeset stayease:0003-create-listing-indexes
CREATE UNIQUE INDEX uk_listing_public_id ON listing(public_id);
CREATE INDEX idx_listing_landlord ON listing(landlord_public_id);
CREATE INDEX idx_listing_category ON listing(category);
CREATE INDEX idx_listing_location ON listing(location);
CREATE INDEX idx_listing_city ON listing(city);
CREATE INDEX idx_listing_country ON listing(country);
CREATE INDEX idx_listing_price ON listing(price);
CREATE INDEX idx_listing_status ON listing(status);
CREATE INDEX idx_listing_is_active ON listing(is_active);
CREATE INDEX idx_listing_created_at ON listing(created_at);
CREATE INDEX idx_listing_image_listing ON listing_image(listing_id);
CREATE UNIQUE INDEX uk_listing_image_public_id ON listing_image(public_id);
CREATE INDEX idx_favorite_user ON favorite_listing(user_public_id);
CREATE INDEX idx_favorite_listing ON favorite_listing(listing_public_id);
--rollback DROP INDEX IF EXISTS uk_listing_public_id, idx_listing_landlord, idx_listing_category, idx_listing_location, idx_listing_city, idx_listing_country, idx_listing_price, idx_listing_status, idx_listing_is_active, idx_listing_created_at, idx_listing_image_listing, uk_listing_image_public_id, idx_favorite_user, idx_favorite_listing;

--changeset stayease:0003-create-listing-sequences
CREATE SEQUENCE listing_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE listing_image_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE favorite_listing_seq START WITH 1 INCREMENT BY 50;
--rollback DROP SEQUENCE IF EXISTS listing_seq, listing_image_seq, favorite_listing_seq;