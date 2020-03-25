import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { AuthenticationService } from './authentification/authentication.service';
import { HttpClient } from '@angular/common/http';
import { AccountStatistiques } from '../domain/statistiques/AccountStatistiques';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class StatistiquesService {
  API_URL = `${environment.apiUrl}/family`;

  constructor(
    private http: HttpClient,
    private authenticationService: AuthenticationService
  ) {
  }

  getStatistiquesAccounts(): Observable<AccountStatistiques[]> {
    const familyName = this.authenticationService.currentUserValue.username;
    const apiUrl = `${this.API_URL}/${familyName}/statistiques/accounts`;
    return this.http.get<AccountStatistiques[]>(apiUrl).pipe(
      map(accountsStat => accountsStat.map(it => new AccountStatistiques(it)))
    );
  }
}
