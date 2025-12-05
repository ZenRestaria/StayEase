import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink, ButtonModule],
  template: `
    <div class="home-page">
      <!-- Hero Section -->
      <section class="hero-section">
        <div class="hero-overlay"></div>
        <div class="hero-content">
          <h1>Not sure where to go? Perfect.</h1>
          <button pButton 
                  label="I'm flexible" 
                  class="flexible-btn"
                  routerLink="/listings">
          </button>
        </div>
      </section>

      <!-- Categories Section -->
      <section class="categories-section">
        <div class="container">
          <h2>Inspiration for your next trip</h2>
          <div class="categories-grid">
            <div class="category-card" 
                 *ngFor="let category of categories"
                 [routerLink]="['/listings']" 
                 [queryParams]="{category: category.value}">
              <div class="category-image">
                <img [src]="category.image" [alt]="category.name">
              </div>
              <div class="category-info">
                <h3>{{ category.name }}</h3>
                <p>{{ category.distance }}</p>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- Hosting Section -->
      <section class="hosting-section">
        <div class="hosting-card">
          <div class="hosting-content">
            <h2>Try hosting</h2>
            <p>Earn extra income and unlock new opportunities by sharing your space.</p>
            <button pButton 
                    label="Learn more" 
                    class="learn-more-btn"
                    routerLink="/create-listing">
            </button>
          </div>
          <div class="hosting-image">
            <img src="https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?q=80&w=2070&auto=format&fit=crop" 
                 alt="Become a host">
          </div>
        </div>
      </section>
    </div>
  `,
  styles: [`
    .home-page {
      background: white;
    }

    .hero-section {
      position: relative;
      height: 640px;
      background-image: url('https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?q=80&w=2070&auto=format&fit=crop');
      background-size: cover;
      background-position: center;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 64px;
    }

    .hero-overlay {
      position: absolute;
      inset: 0;
      background: linear-gradient(to bottom, rgba(0,0,0,0.2), rgba(0,0,0,0.4));
    }

    .hero-content {
      position: relative;
      z-index: 1;
      text-align: center;
      color: white;
      padding: 0 24px;
    }

    .hero-content h1 {
      font-size: 48px;
      font-weight: 600;
      margin: 0 0 32px 0;
      max-width: 600px;
    }

    .flexible-btn {
      background: white;
      color: #222;
      border: none;
      padding: 14px 24px;
      font-size: 16px;
      font-weight: 600;
      border-radius: 32px;
      cursor: pointer;
      transition: transform 0.1s ease;
    }

    .flexible-btn:hover {
      transform: scale(1.04);
    }

    .categories-section {
      padding: 64px 0;
    }

    .container {
      max-width: 1760px;
      margin: 0 auto;
      padding: 0 40px;
    }

    .categories-section h2 {
      font-size: 32px;
      font-weight: 600;
      color: #222;
      margin: 0 0 32px 0;
    }

    .categories-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
      gap: 24px;
    }

    .category-card {
      cursor: pointer;
      text-decoration: none;
      color: inherit;
      transition: transform 0.2s ease;
    }

    .category-card:hover {
      transform: translateY(-4px);
    }

    .category-image {
      position: relative;
      width: 100%;
      padding-top: 100%;
      border-radius: 12px;
      overflow: hidden;
      margin-bottom: 12px;
    }

    .category-image img {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      object-fit: cover;
      transition: transform 0.3s ease;
    }

    .category-card:hover .category-image img {
      transform: scale(1.05);
    }

    .category-info h3 {
      font-size: 18px;
      font-weight: 600;
      color: #222;
      margin: 0 0 4px 0;
    }

    .category-info p {
      font-size: 14px;
      color: #717171;
      margin: 0;
    }

    .hosting-section {
      padding: 64px 0 96px;
    }

    .hosting-card {
      max-width: 1760px;
      margin: 0 auto;
      padding: 0 40px;
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 48px;
      align-items: center;
    }

    .hosting-content h2 {
      font-size: 48px;
      font-weight: 600;
      color: #222;
      margin: 0 0 16px 0;
    }

    .hosting-content p {
      font-size: 18px;
      color: #222;
      line-height: 26px;
      margin: 0 0 32px 0;
    }

    .learn-more-btn {
      background: #222;
      color: white;
      border: none;
      padding: 14px 24px;
      font-size: 16px;
      font-weight: 600;
      border-radius: 8px;
      cursor: pointer;
      transition: transform 0.1s ease;
    }

    .learn-more-btn:hover {
      transform: scale(1.02);
    }

    .hosting-image {
      border-radius: 16px;
      overflow: hidden;
    }

    .hosting-image img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }

    @media (max-width: 1128px) {
      .hero-content h1 {
        font-size: 40px;
      }

      .hosting-card {
        grid-template-columns: 1fr;
      }

      .hosting-content h2 {
        font-size: 40px;
      }
    }

    @media (max-width: 768px) {
      .container,
      .hosting-card {
        padding: 0 24px;
      }

      .hero-section {
        height: 480px;
        margin-bottom: 48px;
      }

      .hero-content h1 {
        font-size: 32px;
      }

      .categories-section {
        padding: 48px 0;
      }

      .categories-section h2 {
        font-size: 26px;
      }

      .categories-grid {
        grid-template-columns: 1fr;
        gap: 32px;
      }

      .hosting-section {
        padding: 48px 0 64px;
      }

      .hosting-content h2 {
        font-size: 32px;
      }

      .hosting-content p {
        font-size: 16px;
      }
    }
  `]
})
export class HomeComponent {
  categories = [
    { 
      name: 'Entire homes', 
      distance: 'Private spaces for your whole group',
      value: 'entire_home',
      image: 'https://images.unsplash.com/photo-1580587771525-78b9dba3b914?q=80&w=1974&auto=format&fit=crop' 
    },
    { 
      name: 'Unique stays', 
      distance: 'One-of-a-kind places to stay',
      value: 'unique',
      image: 'https://images.unsplash.com/photo-1566665797739-1674de7a421a?q=80&w=1974&auto=format&fit=crop' 
    },
    { 
      name: 'Apartments', 
      distance: 'Comfortable city living',
      value: 'apartment',
      image: 'https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?q=80&w=1980&auto=format&fit=crop' 
    },
    { 
      name: 'Villas', 
      distance: 'Luxurious getaways',
      value: 'villa',
      image: 'https://images.unsplash.com/photo-1512917774080-9991f1c4c750?q=80&w=2070&auto=format&fit=crop' 
    }
  ];
}