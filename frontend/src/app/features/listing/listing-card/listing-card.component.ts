import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { RatingModule } from 'primeng/rating';
import { FormsModule } from '@angular/forms';
import { Listing } from '../models/listing.model';

@Component({
  selector: 'app-listing-card',
  standalone: true,
  imports: [CommonModule, CardModule, ButtonModule, TagModule, RatingModule, FormsModule],
  template: `
    <div class="listing-card" (click)="viewDetails()">
      <div class="image-container">
        <img 
          [src]="listing.images && listing.images.length > 0 ? listing.images[0].url : 'https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?q=80&w=2070'" 
          [alt]="listing.title"
          class="listing-image"
        />
        <button class="favorite-btn" (click)="toggleFavorite($event)">
          <i [class]="isFavorite ? 'pi pi-heart-fill' : 'pi pi-heart'"></i>
        </button>
        <span class="category-badge">{{ listing.category }}</span>
      </div>
      
      <div class="card-content">
        <div class="location-rating">
          <h3 class="location">{{ listing.city }}, {{ listing.state }}</h3>
          <div class="rating" *ngIf="listing.averageRating">
            <i class="pi pi-star-fill"></i>
            <span>{{ listing.averageRating | number:'1.2-2' }}</span>
          </div>
        </div>
        
        <p class="title">{{ listing.title }}</p>
        
        <div class="property-details">
          <span>{{ listing.guests }} guests</span>
          <span class="dot">·</span>
          <span>{{ listing.bedrooms }} bedrooms</span>
          <span class="dot">·</span>
          <span>{{ listing.beds }} beds</span>
          <span class="dot">·</span>
          <span>{{ listing.bathrooms }} baths</span>
        </div>
        
        <div class="price-section">
          <span class="price">\${{ listing.price }}</span>
          <span class="period">night</span>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .listing-card {
      cursor: pointer;
      border-radius: 12px;
      transition: transform 0.2s ease, box-shadow 0.2s ease;
      background: white;
    }

    .listing-card:hover {
      transform: translateY(-4px);
      box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
    }

    .image-container {
      position: relative;
      width: 100%;
      padding-top: 66%;
      border-radius: 12px;
      overflow: hidden;
      background: #f7f7f7;
    }

    .listing-image {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      object-fit: cover;
      transition: transform 0.3s ease;
    }

    .listing-card:hover .listing-image {
      transform: scale(1.05);
    }

    .favorite-btn {
      position: absolute;
      top: 12px;
      right: 12px;
      background: rgba(255, 255, 255, 0.9);
      border: none;
      border-radius: 50%;
      width: 32px;
      height: 32px;
      display: flex;
      align-items: center;
      justify-content: center;
      cursor: pointer;
      transition: all 0.2s ease;
      z-index: 2;
    }

    .favorite-btn:hover {
      transform: scale(1.1);
      background: white;
    }

    .favorite-btn i {
      font-size: 16px;
      color: #222;
    }

    .favorite-btn .pi-heart-fill {
      color: #FF385C;
    }

    .category-badge {
      position: absolute;
      bottom: 12px;
      left: 12px;
      background: rgba(255, 255, 255, 0.95);
      padding: 6px 12px;
      border-radius: 20px;
      font-size: 12px;
      font-weight: 600;
      color: #222;
      z-index: 2;
    }

    .card-content {
      padding: 12px 0;
    }

    .location-rating {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 4px;
    }

    .location {
      font-size: 15px;
      font-weight: 600;
      color: #222;
      margin: 0;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      flex: 1;
    }

    .rating {
      display: flex;
      align-items: center;
      gap: 4px;
      font-size: 14px;
      font-weight: 500;
    }

    .rating i {
      font-size: 12px;
      color: #222;
    }

    .rating span {
      color: #222;
    }

    .title {
      font-size: 15px;
      color: #717171;
      margin: 4px 0;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .property-details {
      display: flex;
      align-items: center;
      gap: 6px;
      font-size: 15px;
      color: #717171;
      margin: 4px 0;
      flex-wrap: wrap;
    }

    .dot {
      font-size: 10px;
    }

    .price-section {
      margin-top: 8px;
      display: flex;
      align-items: baseline;
      gap: 4px;
    }

    .price {
      font-size: 16px;
      font-weight: 600;
      color: #222;
    }

    .period {
      font-size: 15px;
      color: #717171;
      font-weight: 400;
    }

    @media (max-width: 768px) {
      .location {
        font-size: 14px;
      }

      .title {
        font-size: 14px;
      }

      .property-details {
        font-size: 14px;
      }

      .price {
        font-size: 15px;
      }
    }
  `]
})
export class ListingCardComponent {
  @Input() listing!: Listing;
  isFavorite = false;

  constructor(private router: Router) {}

  viewDetails(): void {
    this.router.navigate(['/listings', this.listing.publicId]);
  }

  toggleFavorite(event: Event): void {
    event.stopPropagation();
    this.isFavorite = !this.isFavorite;
    // TODO: Implement favorite functionality with backend
  }
}