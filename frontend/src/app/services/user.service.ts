import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { UserAccountDetails } from '../domain/user/UserAccountDetails';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  API_URL = `${environment.apiUrl}/user`;

  constructor(private http: HttpClient) {
  }

  createUser(userAccountDetails: UserAccountDetails): Observable<UserAccountDetails> {
    if (!this.validateUser(userAccountDetails)) {
      return throwError('User is not valid');
    }
    return this.http.post<UserAccountDetails>(`${this.API_URL}/create`, JSON.stringify(userAccountDetails));
  }

  private validateUser(userAccountDetails: UserAccountDetails): boolean {
    if (userAccountDetails.login.trim().length === 0) {
      return false;
    }
    if (userAccountDetails.mail.trim().length === 0) {
      return false;
    }
    if (userAccountDetails.password.trim().length === 0) {
      return false;
    }
    if (userAccountDetails.users.length === 0) {
      return false;
    }
    const nbUserWithSizeZero = userAccountDetails.users
      .filter(user => user.username.trim().length === 0)
      .length;
    return !nbUserWithSizeZero;
  }
}
