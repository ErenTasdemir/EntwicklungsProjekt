import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable, Subject} from 'rxjs';
import {User} from './user.model';
import {tap} from 'rxjs/operators';
import {Router} from '@angular/router';


export interface AuthResponseData {
  id: string;
  username: string;
  name: string;
  lastname: string;
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  user = new BehaviorSubject<User>(null);

  constructor(private http: HttpClient, private router: Router) {
  }

  login(username: string, password: string): Observable<AuthResponseData> {
    return this.http.post<AuthResponseData>('http://localhost:8081/auth/signin', {username, password}).pipe(tap(resData => {
      this.handleAuthenticatioin(resData.id, resData.username, resData.name, resData.lastname, resData.token);
    }));
  }

  register(username: string, password: string, name: string, lastname: string): Observable<AuthResponseData> {
    const body = {
      'username': username,
      'password': password,
      'name': name,
      'lastname': lastname
    };
    return this.http.post<AuthResponseData>('http://localhost:8081/auth/register', body).pipe(tap(resData => {
      this.handleAuthenticatioin(resData.id, resData.username, resData.name, resData.lastname, resData.token);
    }));
  }

  autoLoginOrLogout() {
    const userData: {
      id: string,
      username: string,
      name: string,
      lastname: string,
      token: string,
      tokenExpirationDate: string
    } = JSON.parse(localStorage.getItem('userData'));
    if (!userData) {
      return;
    }

    if (new Date(userData.tokenExpirationDate) < new Date(Date.now())) {
      this.logout();
      return;
    }
    const newExpirationDate: Date = new Date(Date.now());
    newExpirationDate.setHours(newExpirationDate.getHours() + 1);


    const loadedUser = new User(userData.id, userData.username, userData.name, userData.lastname, userData.token, newExpirationDate);
    localStorage.setItem('userData', JSON.stringify(loadedUser));

    if (loadedUser.token) {
      this.user.next(loadedUser);
    }
  }

  logout() {
    this.user.next(null);
    this.router.navigate(['./auth']);
    localStorage.removeItem('userData');
  }

  private handleAuthenticatioin(id: string, username: string, name: string, lastname: string, token: string) {
    const expirationDate: Date = new Date(Date.now());
    expirationDate.setHours(expirationDate.getHours() + 1);
    const user = new User(id, username, name, lastname, token, expirationDate);
    this.user.next(user);
    localStorage.setItem('userData', JSON.stringify(user));
  }
}
