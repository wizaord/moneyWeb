import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Account } from '../domain/account/Account';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  API_URL = `${environment.apiUrl}/accounts`;

  constructor(private http: HttpClient) {
  }

  createAccount(name: string, openDate: string): Observable<Account> {
    const account = {
      accountName: name,
      dateCreate: openDate
    };
    return this.http.post<Account>(`${this.API_URL}/create`, account);
  }

}
