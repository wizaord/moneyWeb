import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { from, Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Account } from '../domain/account/Account';
import { concatAll, concatMap, filter, flatMap, map, reduce, toArray } from 'rxjs/operators';
import { AuthenticationService } from './authentification/authentication.service';
import { TransactionsService } from './transactions.service';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  API_URL = `${environment.apiUrl}/family`;

  constructor(private http: HttpClient,
              private authenticationService: AuthenticationService,
              private transactionsService: TransactionsService) {
  }

  createAccount(name: string, bank: string, openDate: string, ownersSelected: string[]): Observable<Account> {
    const familyName = this.authenticationService.currentUserValue.username;
    const account = {
      accountName: name,
      bankName: bank,
      dateCreate: openDate,
      owners: ownersSelected
    };
    return this.http.post<Account>(`${this.API_URL}/${familyName}/accounts`, account);
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

  getAccountById(accountId: string): Observable<Account> {
    return this.getAccounts()
      .pipe(
        flatMap(account => account),
        filter(account => account.accountName === accountId)
      );
  }

  closAccount(account: Account): Observable<any> {
    const familyName = this.authenticationService.currentUserValue.username;
    const apiUrl = `${this.API_URL}/${familyName}/accounts/${account.accountName}/close`;
    return this.http.patch<Account>(apiUrl, {});
  }

  openAccount(account: Account): Observable<any> {
    const familyName = this.authenticationService.currentUserValue.username;
    const apiUrl = `${this.API_URL}/${familyName}/accounts/${account.accountName}/open`;
    return this.http.patch<Account>(apiUrl, {});

  }

  deleteAccount(account: Account) {
    const familyName = this.authenticationService.currentUserValue.username;
    const apiUrl = `${this.API_URL}/${familyName}/accounts/${account.accountName}`;
    return this.http.delete(apiUrl);
  }

  updateAccountInfo(accountName: string, accountToUpdate: Account) {
    const familyName = this.authenticationService.currentUserValue.username;
    const apiUrl = `${this.API_URL}/${familyName}/accounts/${accountName}`;
    return this.http.put<Account>(apiUrl, accountToUpdate);
  }
}
