import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../../environments/environment';
import { Listing, CreateListing, UpdateListing, SearchListing, Page } from '../models/listing.model';

interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp: string;
}

@Injectable({
  providedIn: 'root'
})
export class ListingService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/listings`;

  getAllListings(page = 0, size = 20): Observable<Page<Listing>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<ApiResponse<Page<Listing>>>(this.apiUrl, { params })
      .pipe(map(response => response.data));
  }

  getListingById(publicId: string): Observable<Listing> {
    return this.http.get<ApiResponse<Listing>>(`${this.apiUrl}/${publicId}`)
      .pipe(map(response => response.data));
  }

  searchListings(searchCriteria: SearchListing): Observable<Page<Listing>> {
    // Build query parameters from search criteria
    let params = new HttpParams();
    
    if (searchCriteria.location) {
      params = params.set('location', searchCriteria.location);
    }
    if (searchCriteria.category) {
      params = params.set('category', searchCriteria.category);
    }
    if (searchCriteria.minPrice !== undefined) {
      params = params.set('minPrice', searchCriteria.minPrice.toString());
    }
    if (searchCriteria.maxPrice !== undefined) {
      params = params.set('maxPrice', searchCriteria.maxPrice.toString());
    }
    if (searchCriteria.guests !== undefined) {
      params = params.set('guests', searchCriteria.guests.toString());
    }
    if (searchCriteria.sortBy) {
      params = params.set('sortBy', searchCriteria.sortBy);
    }
    
    // Pagination
    params = params.set('page', (searchCriteria.page || 0).toString());
    params = params.set('size', (searchCriteria.size || 20).toString());

    return this.http.get<ApiResponse<Page<Listing>>>(`${this.apiUrl}/search`, { params })
      .pipe(map(response => response.data));
  }

  getMyListings(page = 0, size = 20): Observable<Page<Listing>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<ApiResponse<Page<Listing>>>(`${this.apiUrl}/my-listings`, { params })
      .pipe(map(response => response.data));
  }

  createListing(listing: CreateListing): Observable<Listing> {
    return this.http.post<ApiResponse<Listing>>(this.apiUrl, listing)
      .pipe(map(response => response.data));
  }

  updateListing(publicId: string, listing: UpdateListing): Observable<Listing> {
    return this.http.put<ApiResponse<Listing>>(`${this.apiUrl}/${publicId}`, listing)
      .pipe(map(response => response.data));
  }

  deleteListing(publicId: string): Observable<void> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${publicId}`)
      .pipe(map(() => undefined));
  }

  getAllCategories(): Observable<string[]> {
    // For now, return hardcoded categories since backend endpoint doesn't exist
    return new Observable(observer => {
      observer.next(['Entire homes', 'Apartments', 'Villas', 'Unique stays', 'Beachfront']);
      observer.complete();
    });
  }
}