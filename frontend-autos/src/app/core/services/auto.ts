import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AutoService {
  // Apunta a API Gateway
  private apiUrl = 'http://localhost:8080/api/autos';

  constructor(private http: HttpClient) {}

  getAutos(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  createAuto(auto: any): Observable<any> {
    return this.http.post(this.apiUrl, auto);
  }

  updateAuto(id: number, auto: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, auto);
  }

  deleteAuto(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  searchAutos(query?: string, year?: number, brand?: string): Observable<any[]> {
    let params = new HttpParams();
    if (query) params = params.set('query', query);
    if (year) params = params.set('year', year);
    if (brand) params = params.set('brand', brand);
    
    return this.http.get<any[]>(`${this.apiUrl}/search`, { params });
  }
}