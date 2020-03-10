import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { AuthenticationService } from './authentification/authentication.service';
import { Observable } from 'rxjs';
import { Transaction } from '../domain/account/Transaction';
import { filter, flatMap, map, toArray } from 'rxjs/operators';

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
    return this.http.get<Transaction[]>(apiUrl).pipe(
      flatMap(transactions => transactions),
      map(transaction => new Transaction(transaction)),
      map(transaction => {
        transaction.accountName = accountName;
        return transaction;
      }),
      toArray()
    );
  }

  getDistinctTransactionUserLibelle(accountName: string, userLibelleMatch: string): Observable<string[]> {
    return this.getTransactions(accountName)
      .pipe(
        flatMap(t => t),
        filter(transaction => transaction.userLibelle.includes(userLibelleMatch)),
        map(transaction => transaction.userLibelle),
        toArray()
      );
  }

  updateTransaction(transaction: Transaction) {
    console.log('Update transaction => ' + JSON.stringify(transaction));
    const familyName = this.authenticationService.currentUserValue.username;
    const apiUrl = `${this.API_URL}/${familyName}/accounts/${transaction.accountName}/transactions/${transaction.id}`;
    return this.http.patch(apiUrl, transaction);
  }

  removeTransaction(transaction: Transaction): Observable<any> {
    const familyName = this.authenticationService.currentUserValue.username;
    const apiUrl = `${this.API_URL}/${familyName}/accounts/${transaction.accountName}/transactions/${transaction.id}`;
    return this.http.delete(apiUrl);
  }
}
