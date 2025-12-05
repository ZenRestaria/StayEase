import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ListingService } from '../services/listing.service';
import { ListingCardComponent } from '../listing-card/listing-card.component';
import { Listing, Page, SearchListing } from '../models/listing.model';

@Component({
  selector: 'app-listing-search',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ListingCardComponent
  ],
  template: `
    <div class="search-page">
      <!-- Search Header -->
      <div class="search-header">
        <div class="container">
          <div class="search-bar">
            <div class="search-input-group">
              <div class="search-field">
                <label>Where</label>
                <input
                  [(ngModel)]="searchCriteria.location"
                  placeholder="Search destinations"
                  (keyup.enter)="search()" />
              </div>

              <div class="divider"></div>

              <div class="search-field">
                <label>Category</label>
                <select
                  [(ngModel)]="searchCriteria.category"
                  (change)="search()">
                  <option [value]="undefined">Any type</option>
                  <option *ngFor="let cat of categories" [value]="cat">{{ cat }}</option>
                </select>
              </div>

              <div class="divider"></div>

              <div class="search-field">
                <label>Guests</label>
                <input
                  type="number"
                  [(ngModel)]="searchCriteria.guests"
                  placeholder="Add guests"
                  min="1" />
              </div>

              <button class="search-btn" (click)="search()">
                <i class="pi pi-search"></i>
              </button>
            </div>
          </div>

          <!-- Filter Chips -->
          <div class="filter-chips">
            <button
              *ngFor="let cat of popularCategories"
              class="filter-chip"
              [class.active]="searchCriteria.category === cat"
              (click)="selectCategory(cat)">
              {{ cat }}
            </button>
          </div>
        </div>
      </div>

      <!-- Results Section -->
      <div class="container results-section">
        <div class="results-header">
          <h2>{{ listingsPage.totalElements }} stays</h2>
          <div class="sort-controls">
            <select
              [(ngModel)]="searchCriteria.sortBy"
              (change)="search()"
              class="sort-dropdown">
              <option *ngFor="let opt of sortOptions" [value]="opt.value">
                {{ opt.label }}
              </option>
            </select>
          </div>
        </div>

        <!-- Listings Grid -->
        <div class="listings-grid" *ngIf="!loading; else loadingTemplate">
          <app-listing-card
            *ngFor="let listing of listingsPage.content"
            [listing]="listing">
          </app-listing-card>
        </div>

        <!-- Loading Skeleton -->
        <ng-template #loadingTemplate>
          <div class="listings-grid">
            <div class="skeleton-card" *ngFor="let item of [1,2,3,4,5,6,7,8]">
              <div class="skeleton skeleton-image"></div>
              <div class="skeleton skeleton-text mt-2"></div>
              <div class="skeleton skeleton-text-sm mt-1"></div>
              <div class="skeleton skeleton-text-xs mt-2"></div>
            </div>
          </div>
        </ng-template>

        <!-- Pagination -->
        <div class="pagination-container" *ngIf="listingsPage.totalElements > 0">
          <button 
            class="page-btn"
            [disabled]="searchCriteria.page === 0"
            (click)="goToPage(searchCriteria.page! - 1)">
            Previous
          </button>
          
          <span class="page-info">
            Page {{ searchCriteria.page! + 1 }} of {{ listingsPage.totalPages }}
          </span>
          
          <button 
            class="page-btn"
            [disabled]="searchCriteria.page! >= listingsPage.totalPages - 1"
            (click)="goToPage(searchCriteria.page! + 1)">
            Next
          </button>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .search-page {
      min-height: 100vh;
      background: #fff;
    }

    .search-header {
      position: sticky;
      top: 0;
      z-index: 10;
      background: white;
      border-bottom: 1px solid #EBEBEB;
      padding: 20px 0;
    }

    .container {
      max-width: 1760px;
      margin: 0 auto;
      padding: 0 40px;
    }

    .search-bar {
      margin-bottom: 24px;
    }

    .search-input-group {
      display: flex;
      align-items: center;
      border: 1px solid #DDDDDD;
      border-radius: 40px;
      padding: 8px;
      background: white;
      box-shadow: 0 1px 2px rgba(0, 0, 0, 0.08);
      transition: box-shadow 0.2s ease;
    }

    .search-input-group:hover {
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.18);
    }

    .search-field {
      flex: 1;
      padding: 8px 24px;
      display: flex;
      flex-direction: column;
      gap: 4px;
    }

    .search-field label {
      font-size: 12px;
      font-weight: 600;
      color: #222;
    }

    .search-field input,
    .search-field select {
      border: none;
      outline: none;
      font-size: 14px;
      color: #222;
      padding: 0;
      width: 100%;
      background: transparent;
      cursor: pointer;
    }

    .search-field input::placeholder {
      color: #717171;
    }

    .divider {
      width: 1px;
      height: 32px;
      background: #DDDDDD;
    }

    .search-btn {
      background: linear-gradient(to right, #E61E4D 0%, #E31C5F 50%, #D70466 100%);
      border: none;
      border-radius: 50%;
      width: 48px;
      height: 48px;
      display: flex;
      align-items: center;
      justify-content: center;
      cursor: pointer;
      transition: transform 0.1s ease;
    }

    .search-btn:hover {
      transform: scale(1.04);
    }

    .search-btn i {
      color: white;
      font-size: 18px;
    }

    .filter-chips {
      display: flex;
      gap: 12px;
      overflow-x: auto;
      padding: 8px 0;
      -ms-overflow-style: none;
      scrollbar-width: none;
    }

    .filter-chips::-webkit-scrollbar {
      display: none;
    }

    .filter-chip {
      padding: 12px 20px;
      border: 1px solid #DDDDDD;
      border-radius: 24px;
      background: white;
      color: #222;
      font-size: 14px;
      font-weight: 500;
      cursor: pointer;
      white-space: nowrap;
      transition: all 0.2s ease;
    }

    .filter-chip:hover {
      border-color: #222;
    }

    .filter-chip.active {
      background: #222;
      color: white;
      border-color: #222;
    }

    .results-section {
      padding: 40px 40px 80px;
    }

    .results-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 32px;
    }

    .results-header h2 {
      font-size: 24px;
      font-weight: 600;
      color: #222;
      margin: 0;
    }

    .sort-dropdown {
      padding: 10px 16px;
      border: 1px solid #DDDDDD;
      border-radius: 8px;
      font-size: 14px;
      color: #222;
      background: white;
      cursor: pointer;
      outline: none;
    }

    .sort-dropdown:hover {
      border-color: #222;
    }

    .listings-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
      gap: 40px 24px;
    }

    .skeleton-card {
      display: flex;
      flex-direction: column;
    }

    .skeleton {
      background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
      background-size: 200% 100%;
      animation: loading 1.5s infinite;
      border-radius: 12px;
    }

    .skeleton-image {
      height: 250px;
    }

    .skeleton-text {
      height: 20px;
      width: 70%;
    }

    .skeleton-text-sm {
      height: 16px;
      width: 50%;
    }

    .skeleton-text-xs {
      height: 20px;
      width: 40%;
    }

    .mt-1 { margin-top: 8px; }
    .mt-2 { margin-top: 16px; }

    @keyframes loading {
      0% { background-position: 200% 0; }
      100% { background-position: -200% 0; }
    }

    .pagination-container {
      margin-top: 48px;
      display: flex;
      justify-content: center;
      align-items: center;
      gap: 16px;
    }

    .page-btn {
      padding: 10px 20px;
      border: 1px solid #DDDDDD;
      border-radius: 8px;
      background: white;
      color: #222;
      font-size: 14px;
      font-weight: 500;
      cursor: pointer;
      transition: all 0.2s ease;
    }

    .page-btn:hover:not(:disabled) {
      border-color: #222;
      background: #f7f7f7;
    }

    .page-btn:disabled {
      opacity: 0.5;
      cursor: not-allowed;
    }

    .page-info {
      font-size: 14px;
      color: #717171;
    }

    @media (max-width: 1440px) {
      .listings-grid {
        grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
      }
    }

    @media (max-width: 1128px) {
      .listings-grid {
        grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
        gap: 32px 16px;
      }
    }

    @media (max-width: 768px) {
      .container {
        padding: 0 24px;
      }

      .search-input-group {
        flex-direction: column;
        border-radius: 16px;
        padding: 16px;
      }

      .search-field {
        width: 100%;
        padding: 12px 0;
      }

      .divider {
        width: 100%;
        height: 1px;
      }

      .search-btn {
        width: 100%;
        border-radius: 8px;
        margin-top: 12px;
      }

      .listings-grid {
        grid-template-columns: 1fr;
        gap: 32px;
      }

      .results-header {
        flex-direction: column;
        align-items: flex-start;
        gap: 16px;
      }
    }
  `]
})
export class ListingSearchComponent implements OnInit {
  private listingService = inject(ListingService);

  listingsPage: Page<Listing> = {
    content: [],
    totalElements: 0,
    totalPages: 0,
    size: 20,
    number: 0
  };

  searchCriteria: SearchListing = {
    page: 0,
    size: 20,
    sortBy: 'createdAt',
    sortDirection: 'DESC'
  };

  categories: string[] = [];
  loading = false;

  popularCategories = ['Entire homes', 'Apartments', 'Villas', 'Unique stays', 'Beachfront'];

  sortOptions = [
    { label: 'Newest', value: 'createdAt' },
    { label: 'Price: Low to High', value: 'price_asc' },
    { label: 'Price: High to Low', value: 'price_desc' },
    { label: 'Top Rated', value: 'rating' }
  ];

  ngOnInit() {
    this.loadCategories();
    this.search();
  }

  loadCategories() {
    this.listingService.getAllCategories().subscribe(cats => {
      this.categories = cats;
    });
  }

  selectCategory(category: string) {
    this.searchCriteria.category = this.searchCriteria.category === category ? undefined : category;
    this.search();
  }

  search() {
    this.loading = true;
    this.listingService.searchListings(this.searchCriteria).subscribe({
      next: (page) => {
        this.listingsPage = page;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  goToPage(page: number) {
    this.searchCriteria.page = page;
    this.search();
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }
}