import { Component, inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { RouterOutlet, RouterLink, Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { AvatarModule } from 'primeng/avatar';
import { MenuModule } from 'primeng/menu';
import { MenuItem } from 'primeng/api';
import { AuthService } from '../../core/auth/auth.service';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [
    CommonModule, 
    RouterOutlet, 
    RouterLink, 
    ButtonModule, 
    AvatarModule, 
    MenuModule
  ],
  template: `
    <header class="main-header">
      <div class="header-container">
        <!-- Logo -->
        <a routerLink="/" class="logo">
          <i class="pi pi-home"></i>
          <span>StayEase</span>
        </a>

        <!-- Search Bar (Desktop) -->
        <div class="search-bar" *ngIf="!isAuthPage">
          <button class="search-btn" routerLink="/listings">
            <span>Anywhere</span>
            <div class="divider"></div>
            <span>Any week</span>
            <div class="divider"></div>
            <span>Add guests</span>
            <div class="search-icon">
              <i class="pi pi-search"></i>
            </div>
          </button>
        </div>

        <!-- Navigation -->
        <nav class="nav-menu">
          <a routerLink="/create-listing" 
             *ngIf="authService.isAuthenticated() && authService.isLandlord()" 
             class="host-link">
            Become a Host
          </a>
          
          <button pButton 
                  icon="pi pi-globe" 
                  class="p-button-text p-button-rounded globe-btn">
          </button>
          
          <!-- User Menu -->
          <div class="user-menu">
            <button class="menu-btn" (click)="menu.toggle($event)">
              <i class="pi pi-bars"></i>
              <i class="pi pi-user"></i>
            </button>
            
            <p-menu #menu [model]="menuItems" [popup]="true" styleClass="user-dropdown"></p-menu>
          </div>
        </nav>
      </div>
    </header>

    <main class="main-content">
      <router-outlet></router-outlet>
    </main>

    <footer class="main-footer">
      <div class="footer-container">
        <div class="footer-content">
          <p>&copy; 2025 StayEase, Inc. All rights reserved.</p>
          <div class="footer-links">
            <a href="#">Privacy</a>
            <span>·</span>
            <a href="#">Terms</a>
            <span>·</span>
            <a href="#">Sitemap</a>
          </div>
        </div>
      </div>
    </footer>
  `,
  styles: [`
    .main-header {
      position: sticky;
      top: 0;
      z-index: 100;
      background: white;
      border-bottom: 1px solid #EBEBEB;
      box-shadow: 0 1px 0 rgba(0, 0, 0, 0.08);
    }

    .header-container {
      max-width: 1760px;
      margin: 0 auto;
      padding: 0 40px;
      height: 80px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 24px;
    }

    .logo {
      display: flex;
      align-items: center;
      gap: 8px;
      text-decoration: none;
      color: #FF385C;
      font-size: 20px;
      font-weight: 700;
      white-space: nowrap;
    }

    .logo i {
      font-size: 32px;
    }

    .search-bar {
      flex: 1;
      display: flex;
      justify-content: center;
      max-width: 850px;
    }

    .search-btn {
      display: flex;
      align-items: center;
      gap: 16px;
      padding: 8px 8px 8px 24px;
      border: 1px solid #DDDDDD;
      border-radius: 40px;
      background: white;
      cursor: pointer;
      transition: box-shadow 0.2s ease;
      font-size: 14px;
      font-weight: 500;
      color: #222;
    }

    .search-btn:hover {
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.18);
    }

    .search-btn .divider {
      width: 1px;
      height: 24px;
      background: #DDDDDD;
    }

    .search-icon {
      background: #FF385C;
      border-radius: 50%;
      width: 32px;
      height: 32px;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .search-icon i {
      color: white;
      font-size: 12px;
    }

    .nav-menu {
      display: flex;
      align-items: center;
      gap: 8px;
    }

    .host-link {
      padding: 12px 16px;
      border-radius: 24px;
      font-size: 14px;
      font-weight: 500;
      color: #222;
      text-decoration: none;
      transition: background 0.2s ease;
      white-space: nowrap;
    }

    .host-link:hover {
      background: #F7F7F7;
    }

    .globe-btn {
      color: #222;
    }

    .user-menu {
      position: relative;
    }

    .menu-btn {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 8px 8px 8px 12px;
      border: 1px solid #DDDDDD;
      border-radius: 24px;
      background: white;
      cursor: pointer;
      transition: box-shadow 0.2s ease;
    }

    .menu-btn:hover {
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.18);
    }

    .menu-btn i {
      color: #222;
    }

    .menu-btn .pi-bars {
      font-size: 16px;
    }

    .menu-btn .pi-user {
      font-size: 24px;
      color: #717171;
    }

    .main-content {
      min-height: calc(100vh - 160px);
    }

    .main-footer {
      background: #F7F7F7;
      border-top: 1px solid #EBEBEB;
      padding: 24px 0;
    }

    .footer-container {
      max-width: 1760px;
      margin: 0 auto;
      padding: 0 40px;
    }

    .footer-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 14px;
      color: #222;
    }

    .footer-content p {
      margin: 0;
    }

    .footer-links {
      display: flex;
      align-items: center;
      gap: 8px;
    }

    .footer-links a {
      color: #222;
      text-decoration: none;
    }

    .footer-links a:hover {
      text-decoration: underline;
    }

    .footer-links span {
      color: #717171;
    }

    @media (max-width: 1128px) {
      .search-bar {
        display: none;
      }

      .header-container {
        padding: 0 24px;
      }
    }

    @media (max-width: 768px) {
      .host-link {
        display: none;
      }

      .logo span {
        display: none;
      }

      .footer-content {
        flex-direction: column;
        gap: 12px;
        text-align: center;
      }
    }

    ::ng-deep {
      .user-dropdown {
        margin-top: 12px;
        border-radius: 12px;
        box-shadow: 0 2px 16px rgba(0, 0, 0, 0.12);
        padding: 8px 0;
      }

      .user-dropdown .p-menuitem-link {
        padding: 12px 16px;
        font-size: 14px;
      }
    }
  `]
})
export class MainLayoutComponent {
  authService = inject(AuthService);
  private router = inject(Router);
  private platformId = inject(PLATFORM_ID);

  get isAuthPage(): boolean {
    if (isPlatformBrowser(this.platformId)) {
      const url = window.location.pathname;
      return url.includes('/login') || url.includes('/register');
    }
    return false;
  }

  menuItems: MenuItem[] = [
    {
      label: 'Sign up',
      icon: 'pi pi-user-plus',
      command: () => this.navigateTo('/register'),
      visible: !this.authService.isAuthenticated()
    },
    {
      label: 'Log in',
      icon: 'pi pi-sign-in',
      command: () => this.navigateTo('/login'),
      visible: !this.authService.isAuthenticated()
    },
    {
      separator: true,
      visible: !this.authService.isAuthenticated()
    },
    {
      label: 'My Listings',
      icon: 'pi pi-list',
      command: () => this.navigateTo('/my-listings'),
      visible: this.authService.isAuthenticated() && this.authService.isLandlord()
    },
    {
      label: 'My Bookings',
      icon: 'pi pi-calendar',
      command: () => this.navigateTo('/bookings'),
      visible: this.authService.isAuthenticated()
    },
    {
      label: 'Account',
      icon: 'pi pi-cog',
      command: () => this.navigateTo('/profile'),
      visible: this.authService.isAuthenticated()
    },
    {
      separator: true,
      visible: this.authService.isAuthenticated()
    },
    {
      label: 'Log out',
      icon: 'pi pi-sign-out',
      command: () => this.authService.logout(),
      visible: this.authService.isAuthenticated()
    }
  ];

  navigateTo(path: string): void {
    this.router.navigate([path]);
  }
}