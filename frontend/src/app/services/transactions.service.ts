import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { AuthenticationService } from './authentification/authentication.service';
import { Observable } from 'rxjs';
import { filter, flatMap } from 'rxjs/operators';

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


  getTransactionsByDate(accountName: string, beginDate: Date, endDate: Date): Observable<Transaction> {
    const beginDateTime = beginDate.getTime();
    const endDateTime = endDate.getTime();
    return this.getTransactions(accountName)
      .pipe(
        flatMap(transactions => transactions),
        filter(transaction => {
          const transactionDate = new Date(transaction.dateCreation).getTime();
          return (transactionDate > beginDateTime && transactionDate < endDateTime);
        })
      );
  }
}
