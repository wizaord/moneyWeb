import { Injectable } from '@angular/core';
import { User } from '../../domain/authenticated/user';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private currentUserSubject: BehaviorSubject<User>;
  public currentUser: Observable<User>;

  constructor(private http: HttpClient) {
    this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('currentUser')));
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): User {
    let user: User;
    this.currentUser.subscribe(
      it => {
        user = it;
      }
    );
    return user;
  }

  login(username: string, password: string): Observable<any> {
    return this.http.post<any>(`${environment.apiUrl}/authenticate`, { username, password })
      .pipe(map(token => {

        const userToStore = new User();
        userToStore.username = username;
        userToStore.token = token.token;

        localStorage.setItem('currentUser', JSON.stringify(userToStore));
        this.currentUserSubject.next(userToStore);
        return token;
      }));
  }

  logout() {
    // remove user from local storage to log user out
    console.log('Suppression du user du localStorage');
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
  }
}
