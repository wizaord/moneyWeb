import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { AuthenticationService } from './authentification/authentication.service';
import { Observable } from 'rxjs';
import { Transaction } from '../domain/account/Transaction';
import { distinct, filter, flatMap, map, toArray } from 'rxjs/operators';
import { Account } from '../domain/account/Account';
import { AccountService } from './account.service';

@Injectable({
  providedIn: 'root'
})
export class TransactionsService {
  API_URL = `${environment.apiUrl}/family`;

  constructor(private http: HttpClient,
              private authenticationService: AuthenticationService,
              private accountService: AccountService) {
  }

  getTransactionsByAccount(account: Account): Observable<Transaction[]> {
    const familyName = this.authenticationService.currentUserValue.username;
    const apiUrl = `${this.API_URL}/${familyName}/accounts/${account.accountName}/transactions`;
    return this.http.get<Transaction[]>(apiUrl).pipe(
      flatMap(transactions => transactions),
      map(transaction => Transaction.fromTransaction(transaction)),
      map(transaction => {
        transaction.accountName = account.accountName;
        transaction.owners = account.owners;
        return transaction;
      }),
      toArray()
    );
  }

  getFlattenTransaction(account: Account): Observable<Transaction> {
    return this.getTransactionsByAccount(account).pipe(
      flatMap(value => value)
    );
  }

  getMatchTransactionsBasedOnUserLibelle(account: Account, userLibelleMatch: string): Observable<Transaction[]> {
    return this.getFlattenTransaction(account)
      .pipe(
        filter(transaction => transaction.userLibelle.toLowerCase().includes(userLibelleMatch.toLowerCase())),
        distinct(transaction => transaction.userLibelle),
        toArray(),
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

  getAllTransactions(): Observable<Transaction[]> {
    return this.accountService.getAccounts().pipe(
      flatMap(a => a),
      flatMap(account => this.getFlattenTransaction(account)),
      toArray()
    );
  }
}
