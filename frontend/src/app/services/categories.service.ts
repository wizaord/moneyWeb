import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { AuthenticationService } from './authentification/authentication.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CategoriesService {

  API_URL = `${environment.apiUrl}/categories`;

  constructor(private http: HttpClient,
              private authenticationService: AuthenticationService) {
  }

  getCategories(): Observable<CategoryFamily[]> {
    return this.http.get<CategoryFamily[]>(this.API_URL);
  }
}
