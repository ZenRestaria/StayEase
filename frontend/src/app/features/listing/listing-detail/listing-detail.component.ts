import { Component, OnInit, Input, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { GalleriaModule } from 'primeng/galleria';
import { CalendarModule } from 'primeng/calendar';
import { SkeletonModule } from 'primeng/skeleton';
import { DividerModule } from 'primeng/divider';
import { ListingService } from '../services/listing.service';

export interface ListingDetail {
  publicId: string;
  title: string;
  description: string;
  images: Array<{url: string; caption?: string}>;
  city: string;
  state: string;
  country: string;
  guests: number;
  bedrooms: number;
  beds: number;
  bathrooms: number;
  amenities: string[];
  price: number;
  currency: string;
  averageRating: number;
  reviewCount: number;
  landlord: {
    firstName: string;
    lastName: string;
    imageUrl?: string;
  };
  checkInTime?: string;
  checkOutTime?: string;
  minNights?: number;
  cleaningFee?: number;
  serviceFeePercentage?: number;
}

@Component({
  selector: 'app-listing-detail',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ButtonModule,
    GalleriaModule,
    CalendarModule,
    SkeletonModule,
    DividerModule
  ],
  template: `
    <div class="detail-page" *ngIf="!loading && listing; else loadingTemplate">
      <!-- Image Gallery -->
      <div class="gallery-container">
        <div class="images-grid">
          <div class="main-image" (click)="showGalleria = true">
            <img [src]="listing.images[0]?.url" [alt]="listing.title">
          </div>
          <div class="side-images">
            <div class="side-image" *ngFor="let img of listing.images.slice(1, 5)" (click)="showGalleria = true">
              <img [src]="img.url" [alt]="listing.title">
            </div>
          </div>
          <button class="show-all-btn" (click)="showGalleria = true">
            <i class="pi pi-th-large"></i>
            Show all photos
          </button>
        </div>
      </div>

      <!-- Fullscreen Galleria -->
      <p-galleria
        [value]="listing.images"
        [(visible)]="showGalleria"
        [numVisible]="5"
        [circular]="true"
        [fullScreen]="true"
        [showItemNavigators]="true"
        [showThumbnails]="true">
        <ng-template pTemplate="item" let-item>
          <img [src]="item.url" class="galleria-image">
        </ng-template>
        <ng-template pTemplate="thumbnail" let-item>
          <img [src]="item.url" class="galleria-thumbnail">
        </ng-template>
      </p-galleria>

      <!-- Content Section -->
      <div class="content-container">
        <div class="main-content">
          <!-- Header -->
          <div class="listing-header">
            <div>
              <h1>{{ listing.title }}</h1>
              <div class="header-info">
                <span class="rating" *ngIf="listing.averageRating">
                  <i class="pi pi-star-fill"></i>
                  {{ listing.averageRating | number:'1.2-2' }}
                </span>
                <span class="dot">·</span>
                <span class="reviews">{{ listing.reviewCount }} reviews</span>
                <span class="dot">·</span>
                <span class="location">{{ listing.city }}, {{ listing.state }}, {{ listing.country }}</span>
              </div>
            </div>
          </div>

          <p-divider></p-divider>

          <!-- Host Info -->
          <div class="host-section">
            <div class="host-info">
              <img [src]="listing.landlord.imageUrl || 'https://via.placeholder.com/56'" 
                   [alt]="listing.landlord.firstName" 
                   class="host-avatar">
              <div>
                <h3>Hosted by {{ listing.landlord.firstName }} {{ listing.landlord.lastName }}</h3>
              </div>
            </div>
          </div>

          <p-divider></p-divider>

          <!-- Property Details -->
          <div class="property-details">
            <div class="detail-item">
              <i class="pi pi-users"></i>
              <div>
                <strong>{{ listing.guests }} guests</strong>
                <p>Perfect for groups or families</p>
              </div>
            </div>
            <div class="detail-item">
              <i class="pi pi-home"></i>
              <div>
                <strong>{{ listing.bedrooms }} bedrooms · {{ listing.beds }} beds · {{ listing.bathrooms }} baths</strong>
                <p>Comfortable sleeping arrangements</p>
              </div>
            </div>
          </div>

          <p-divider></p-divider>

          <!-- Description -->
          <div class="description-section">
            <h2>About this place</h2>
            <p>{{ listing.description }}</p>
          </div>

          <p-divider></p-divider>

          <!-- Amenities -->
          <div class="amenities-section" *ngIf="listing.amenities?.length">
            <h2>What this place offers</h2>
            <div class="amenities-grid">
              <div class="amenity" *ngFor="let amenity of listing.amenities">
                <i class="pi pi-check"></i>
                <span>{{ amenity }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- Booking Card (Sticky) -->
        <div class="booking-sidebar">
          <div class="booking-card">
            <div class="price-header">
              <div>
                <span class="price">\${{ listing.price }}</span>
                <span class="period">night</span>
              </div>
              <div class="rating-info" *ngIf="listing.averageRating">
                <i class="pi pi-star-fill"></i>
                <span>{{ listing.averageRating | number:'1.2-2' }}</span>
                <span class="dot">·</span>
                <span class="reviews-count">{{ listing.reviewCount }} reviews</span>
              </div>
            </div>

            <div class="date-selection">
              <div class="date-input-group">
                <div class="date-input">
                  <label>CHECK-IN</label>
                  <p-calendar
                    [(ngModel)]="checkInDate" 
                    [minDate]="today"
                    placeholder="Add date"
                    dateFormat="mm/dd/yy">
                  </p-calendar>
                </div>
                <div class="date-divider"></div>
                <div class="date-input">
                  <label>CHECKOUT</label>
                  <p-calendar
                    [(ngModel)]="checkOutDate" 
                    [minDate]="checkInDate || today"
                    placeholder="Add date"
                    dateFormat="mm/dd/yy">
                  </p-calendar>
                </div>
              </div>
              
              <div class="guests-input">
                <label>GUESTS</label>
                <input type="number" [(ngModel)]="guestCount" [max]="listing.guests" min="1" placeholder="1 guest">
              </div>
            </div>

            <button 
              pButton 
              label="Reserve" 
              class="reserve-btn"
              [disabled]="!checkInDate || !checkOutDate"
              (click)="reserve()">
            </button>

            <p class="no-charge-text">You won't be charged yet</p>

            <div class="price-breakdown" *ngIf="checkInDate && checkOutDate">
              <div class="breakdown-item">
                <span>\${{ listing.price }} × {{ calculateNights() }} nights</span>
                <span>\${{ listing.price * calculateNights() }}</span>
              </div>
              <div class="breakdown-item" *ngIf="listing.cleaningFee">
                <span>Cleaning fee</span>
                <span>\${{ listing.cleaningFee }}</span>
              </div>
              <div class="breakdown-item" *ngIf="listing.serviceFeePercentage">
                <span>Service fee</span>
                <span>\${{ calculateServiceFee() }}</span>
              </div>
              <p-divider></p-divider>
              <div class="breakdown-item total">
                <strong>Total</strong>
                <strong>\${{ calculateTotal() }}</strong>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <ng-template #loadingTemplate>
      <div class="loading-container">
        <p-skeleton height="500px" borderRadius="16px"></p-skeleton>
        <div class="container">
          <p-skeleton height="40px" width="60%" class="mt-4"></p-skeleton>
          <p-skeleton height="20px" width="40%" class="mt-2"></p-skeleton>
        </div>
      </div>
    </ng-template>
  `,
  styles: [`
    .detail-page {
      background: white;
    }

    .gallery-container {
      max-width: 1760px;
      margin: 0 auto;
      padding: 24px 80px;
    }

    .images-grid {
      position: relative;
      display: grid;
      grid-template-columns: 2fr 1fr 1fr;
      gap: 8px;
      height: 560px;
      border-radius: 12px;
      overflow: hidden;
    }

    .main-image {
      grid-row: 1 / 3;
      cursor: pointer;
      overflow: hidden;
    }

    .main-image img {
      width: 100%;
      height: 100%;
      object-fit: cover;
      transition: transform 0.3s ease;
    }

    .main-image:hover img {
      transform: scale(1.05);
    }

    .side-images {
      grid-column: 2 / 4;
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 8px;
    }

    .side-image {
      cursor: pointer;
      overflow: hidden;
      height: 276px;
    }

    .side-image img {
      width: 100%;
      height: 100%;
      object-fit: cover;
      transition: transform 0.3s ease;
    }

    .side-image:hover img {
      transform: scale(1.05);
    }

    .show-all-btn {
      position: absolute;
      bottom: 24px;
      right: 24px;
      background: white;
      border: 1px solid #222;
      border-radius: 8px;
      padding: 12px 20px;
      font-size: 14px;
      font-weight: 600;
      cursor: pointer;
      display: flex;
      align-items: center;
      gap: 8px;
      transition: all 0.2s ease;
    }

    .show-all-btn:hover {
      background: #f7f7f7;
    }

    .content-container {
      max-width: 1760px;
      margin: 0 auto;
      padding: 48px 80px;
      display: grid;
      grid-template-columns: 1fr 400px;
      gap: 100px;
    }

    .main-content {
      max-width: 700px;
    }

    .listing-header h1 {
      font-size: 26px;
      font-weight: 600;
      color: #222;
      margin: 0 0 8px 0;
    }

    .header-info {
      display: flex;
      align-items: center;
      gap: 8px;
      font-size: 14px;
      color: #222;
    }

    .rating i {
      font-size: 12px;
      margin-right: 4px;
    }

    .dot {
      color: #717171;
    }

    .location {
      text-decoration: underline;
      cursor: pointer;
    }

    .host-section {
      padding: 32px 0;
    }

    .host-info {
      display: flex;
      align-items: center;
      gap: 16px;
    }

    .host-avatar {
      width: 56px;
      height: 56px;
      border-radius: 50%;
      object-fit: cover;
    }

    .host-info h3 {
      font-size: 22px;
      font-weight: 600;
      color: #222;
      margin: 0;
    }

    .property-details {
      padding: 32px 0;
      display: flex;
      flex-direction: column;
      gap: 24px;
    }

    .detail-item {
      display: flex;
      align-items: flex-start;
      gap: 16px;
    }

    .detail-item i {
      font-size: 24px;
      color: #222;
    }

    .detail-item strong {
      font-size: 16px;
      color: #222;
      display: block;
      margin-bottom: 4px;
    }

    .detail-item p {
      font-size: 14px;
      color: #717171;
      margin: 0;
    }

    .description-section {
      padding: 32px 0;
    }

    .description-section h2 {
      font-size: 22px;
      font-weight: 600;
      color: #222;
      margin: 0 0 24px 0;
    }

    .description-section p {
      font-size: 16px;
      line-height: 24px;
      color: #222;
    }

    .amenities-section {
      padding: 32px 0;
    }

    .amenities-section h2 {
      font-size: 22px;
      font-weight: 600;
      color: #222;
      margin: 0 0 24px 0;
    }

    .amenities-grid {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 16px;
    }

    .amenity {
      display: flex;
      align-items: center;
      gap: 16px;
      font-size: 16px;
      color: #222;
    }

    .amenity i {
      font-size: 20px;
    }

    .booking-sidebar {
      position: sticky;
      top: 100px;
      height: fit-content;
    }

    .booking-card {
      border: 1px solid #DDDDDD;
      border-radius: 12px;
      padding: 24px;
      box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
    }

    .price-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 24px;
    }

    .price {
      font-size: 22px;
      font-weight: 600;
      color: #222;
    }

    .period {
      font-size: 16px;
      color: #222;
      font-weight: 400;
    }

    .rating-info {
      display: flex;
      align-items: center;
      gap: 4px;
      font-size: 14px;
    }

    .rating-info i {
      font-size: 12px;
      color: #222;
    }

    .date-selection {
      border: 1px solid #B0B0B0;
      border-radius: 8px;
      overflow: hidden;
      margin-bottom: 16px;
    }

    .date-input-group {
      display: grid;
      grid-template-columns: 1fr auto 1fr;
      border-bottom: 1px solid #B0B0B0;
    }

    .date-input {
      padding: 12px;
    }

    .date-input label {
      display: block;
      font-size: 10px;
      font-weight: 600;
      color: #222;
      margin-bottom: 4px;
    }

    .date-divider {
      width: 1px;
      background: #B0B0B0;
    }

    .guests-input {
      padding: 12px;
    }

    .guests-input label {
      display: block;
      font-size: 10px;
      font-weight: 600;
      color: #222;
      margin-bottom: 4px;
    }

    .guests-input input {
      width: 100%;
      border: none;
      outline: none;
      font-size: 14px;
      color: #222;
    }

    .reserve-btn {
      width: 100%;
      background: linear-gradient(to right, #E61E4D 0%, #E31C5F 50%, #D70466 100%);
      border: none;
      padding: 14px;
      font-size: 16px;
      font-weight: 600;
      border-radius: 8px;
      cursor: pointer;
      transition: transform 0.1s ease;
    }

    .reserve-btn:hover:not(:disabled) {
      transform: scale(1.02);
    }

    .reserve-btn:disabled {
      opacity: 0.6;
      cursor: not-allowed;
    }

    .no-charge-text {
      text-align: center;
      font-size: 14px;
      color: #717171;
      margin: 12px 0 24px 0;
    }

    .price-breakdown {
      margin-top: 24px;
    }

    .breakdown-item {
      display: flex;
      justify-content: space-between;
      margin-bottom: 16px;
      font-size: 16px;
      color: #222;
    }

    .breakdown-item.total {
      font-size: 16px;
      padding-top: 24px;
    }

    .loading-container {
      max-width: 1760px;
      margin: 0 auto;
      padding: 24px 80px;
    }

    @media (max-width: 1128px) {
      .content-container {
        grid-template-columns: 1fr;
        gap: 48px;
      }

      .booking-sidebar {
        position: relative;
        top: 0;
      }
    }

    @media (max-width: 768px) {
      .gallery-container,
      .content-container,
      .loading-container {
        padding: 24px;
      }

      .images-grid {
        grid-template-columns: 1fr;
        grid-template-rows: auto;
        height: auto;
      }

      .main-image {
        grid-row: 1;
        height: 300px;
      }

      .side-images {
        display: none;
      }

      .listing-header h1 {
        font-size: 22px;
      }

      .amenities-grid {
        grid-template-columns: 1fr;
      }
    }

    ::ng-deep {
      .p-calendar .p-inputtext {
        border: none;
        padding: 0;
        font-size: 14px;
        color: #222;
        width: 100%;
      }

      .p-calendar .p-inputtext::placeholder {
        color: #717171;
      }

      .galleria-image {
        max-height: 80vh;
        width: auto;
        object-fit: contain;
      }

      .galleria-thumbnail {
        height: 80px;
        object-fit: cover;
      }
    }
  `]
})
export class ListingDetailComponent implements OnInit {
  @Input() id!: string;

  private listingService = inject(ListingService);
  private router = inject(Router);

  listing: ListingDetail | null = null;
  loading = true;
  showGalleria = false;
  checkInDate: Date | null = null;
  checkOutDate: Date | null = null;
  guestCount = 1;
  today = new Date();

  ngOnInit() {
    if (this.id) {
      this.loadListing();
    } else {
      this.router.navigate(['/listings']);
    }
  }

  loadListing() {
    this.listingService.getListingById(this.id).subscribe({
      next: (listing: any) => {
        this.listing = listing;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.router.navigate(['/listings']);
      }
    });
  }

  calculateNights(): number {
    if (!this.checkInDate || !this.checkOutDate) return 0;
    const diff = this.checkOutDate.getTime() - this.checkInDate.getTime();
    return Math.ceil(diff / (1000 * 3600 * 24));
  }

  calculateServiceFee(): number {
    if (!this.listing?.serviceFeePercentage) return 0;
    const nights = this.calculateNights();
    const subtotal = this.listing.price * nights;
    return (subtotal * this.listing.serviceFeePercentage) / 100;
  }

  calculateTotal(): number {
    if (!this.listing) return 0;
    const nights = this.calculateNights();
    const subtotal = this.listing.price * nights;
    const cleaningFee = this.listing.cleaningFee || 0;
    const serviceFee = this.calculateServiceFee();
    return subtotal + cleaningFee + serviceFee;
  }

  reserve() {
    if (this.checkInDate && this.checkOutDate && this.listing) {
      console.log('Booking:', {
        listingId: this.listing.publicId,
        checkIn: this.checkInDate,
        checkOut: this.checkOutDate,
        guests: this.guestCount,
        total: this.calculateTotal()
      });
      // TODO: Navigate to booking page or implement booking logic
    }
  }
}