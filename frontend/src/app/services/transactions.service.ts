import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { AuthenticationService } from './authentification/authentication.service';
import { Observable } from 'rxjs';
import { filter, flatMap, toArray } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class TransactionsService {
  API_URL = `${environment.apiUrl}/family`;

  constructor(private http: HttpClient,
              private authenticationService: AuthenticationService) {
  }

  getTransactions(accountName: string): Observable<Transaction[]> {
    const familyName = this.authenticationService.currentUserValue.username;
    const apiUrl = `${this.API_URL}/${familyName}/accounts/${accountName}/transactions`;
    return this.http.get<Transaction[]>(apiUrl);
  }
}
