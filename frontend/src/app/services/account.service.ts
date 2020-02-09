import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Account } from '../domain/account/Account';
import { concatAll, concatMap, filter, flatMap, toArray } from 'rxjs/operators';
import { AuthenticationService } from './authentification/authentication.service';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  API_URL = `${environment.apiUrl}/family`;

  constructor(private http: HttpClient,
              private authenticationService: AuthenticationService) {
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
    const familyName = this.authenticationService.currentUserValue.username;
    const apiUrl = `${this.API_URL}/${familyName}/accounts`;
    return this.http.get<Account[]>(apiUrl);
  }

  getOpenedAccounts(): Observable<Account> {
    return this.getAccounts()
      .pipe(
        flatMap(account => account),
        filter(account => account.isOpened),
      );
  }

}
