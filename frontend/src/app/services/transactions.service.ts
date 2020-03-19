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
      map(transaction => Transaction.fromTransaction(transaction)),
      map(transaction => {
        transaction.accountName = accountName;
        return transaction;
      }),
      toArray()
    );
  }

  getFlattenTransaction(accountName: string): Observable<Transaction> {
    return this.getTransactions(accountName).pipe(
      flatMap(value => value)
    );
  }

  getMatchTransactionsBasedOnUserLibelle(accountName: string, userLibelleMatch: string): Observable<Transaction[]> {
    return this.getFlattenTransaction(accountName)
      .pipe(
        filter(transaction => transaction.userLibelle.toLowerCase().includes(userLibelleMatch.toLowerCase())),
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

  createTransaction(transaction: Transaction): Observable<Transaction> {
    console.log('create transaction => ' + JSON.stringify(transaction));
    const familyName = this.authenticationService.currentUserValue.username;
    const apiUrl = `${this.API_URL}/${familyName}/accounts/${transaction.accountName}/transactions`;
    return this.http.post<Transaction>(apiUrl, transaction)
      .pipe(
        map(t => Transaction.fromTransaction(t)),
        map(t => {
          t.accountName = transaction.accountName;
          return t;
        })
      );
  }
}
