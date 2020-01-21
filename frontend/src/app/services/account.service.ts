import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Account } from '../domain/account/Account';
import { concatAll, concatMap, filter, flatMap, toArray } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  API_URL = `${environment.apiUrl}/accounts`;

  constructor(private http: HttpClient) {
  }

  createAccount(name: string, bank: string, openDate: string, ownersSelected: string[]): Observable<Account> {
    const account = {
      accountName: name,
      bankName: bank,
      dateCreate: openDate,
      owners: ownersSelected
    };
    return this.http.post<Account>(`${this.API_URL}/create`, account);
  }

  getAccounts(): Observable<Account[]> {
    return this.http.get<Account[]>(`${this.API_URL}`);
  }

  getOpenedAccounts(): Observable<Account[]> {
    return this.http.get<Account[]>(`${this.API_URL}`)
      .pipe(
        flatMap(x => x),
        filter(account => account.isOpened),
        toArray()
      );
  }

}
