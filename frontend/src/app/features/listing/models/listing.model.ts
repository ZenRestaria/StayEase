export interface Listing {
  publicId: string;
  id?: string; // For backward compatibility
  landlordPublicId: string;
  title: string;
  description: string;
  
  // Location
  location: string;
  address?: string;
  city: string;
  state?: string;
  country: string;
  postalCode?: string;
  latitude?: number;
  longitude?: number;
  
  // Property details
  guests: number;
  bedrooms: number;
  beds: number;
  bathrooms: number;
  squareFeet?: number;
  
  // Pricing
  price: number;
  currency: string;
  cleaningFee?: number;
  serviceFeePercentage?: number;
  
  // Category and type
  category: string;
  propertyType?: string;
  roomType?: string;
  
  // Rules and amenities
  amenities: string[];
  rules?: string;
  houseRules?: Record<string, boolean>;
  
  // Booking rules
  checkInTime?: string;
  checkOutTime?: string;
  minNights?: number;
  maxNights?: number;
  instantBook?: boolean;
  cancellationPolicy?: string;
  
  // Status and statistics
  status?: string;
  isActive?: boolean;
  viewCount?: number;
  bookingCount?: number;
  favoriteCount?: number;
  averageRating?: number;
  reviewCount?: number;
  
  // Images
  images: ListingImage[];
  
  // Timestamps
  createdAt: string;
  updatedAt: string;
  publishedAt?: string;
  
  // Landlord info (optional, when needed)
  landlord?: {
    publicId: string;
    firstName: string;
    lastName: string;
    email?: string;
    imageUrl?: string;
  };
}

export interface ListingImage {
  publicId?: string;
  id?: string; // For backward compatibility
  url: string;
  imageUrl?: string; // For backward compatibility
  caption?: string;
  displayOrder?: number;
  isPrimary?: boolean;
  createdAt?: string;
}

export interface CreateListing {
  title: string;
  description: string;
  
  // Location
  address: string;
  city: string;
  state?: string;
  country: string;
  postalCode?: string;
  latitude?: number;
  longitude?: number;
  
  // Property details
  maxGuests: number;
  bedrooms: number;
  beds: number;
  bathrooms: number;
  squareFeet?: number;
  
  // Pricing
  basePrice: number;
  currency?: string;
  cleaningFee?: number;
  serviceFeePercentage?: number;
  
  // Type
  propertyType: string;
  roomType: string;
  
  // Amenities and rules
  amenities?: string[];
  houseRules?: Record<string, boolean>;
  
  // Booking rules
  checkInTime?: string;
  checkOutTime?: string;
  minNights?: number;
  maxNights?: number;
  instantBook?: boolean;
  cancellationPolicy?: string;
  
  // Images
  images?: Array<{
    imageUrl: string;
    caption?: string;
    displayOrder?: number;
    isPrimary?: boolean;
  }>;
}

export interface UpdateListing {
  title?: string;
  description?: string;
  address?: string;
  city?: string;
  state?: string;
  country?: string;
  guests?: number;
  bedrooms?: number;
  beds?: number;
  bathrooms?: number;
  basePrice?: number;
  category?: string;
  amenities?: string[];
}

export interface SearchListing {
  location?: string;
  city?: string;
  country?: string;
  category?: string;
  minPrice?: number;
  maxPrice?: number;
  guests?: number;
  page?: number;
  size?: number;
  sortBy?: string;
  sortDirection?: string;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}