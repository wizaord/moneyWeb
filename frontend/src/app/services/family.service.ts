import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { AccountOwner } from '../domain/user/AccountOwner';
import { HttpClient } from '@angular/common/http';
import { AuthenticationService } from './authentification/authentication.service';

@Injectable({
  providedIn: 'root'
})
export class FamilyService {
  API_URL = `${environment.apiUrl}/family`;

  constructor(private http: HttpClient,
              private authenticationService: AuthenticationService) {
  }

  getOwners(): Observable<AccountOwner[]> {
    const familyName = this.authenticationService.currentUserValue.username;
    return this.http.get<AccountOwner[]>(`${this.API_URL}/${familyName}/owners`);
  }

}
