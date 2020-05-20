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
    console.log(username);
    console.log(password);
    console.log(name);
    console.log(lastname);
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

  autoLogin() {
    const userData: {
      id: string,
      username: string,
      name: string,
      lastname: string,
      token: string
    } = JSON.parse(localStorage.getItem('userData'));
    if (!userData) {
      return;
    }

    const loadedUser = new User(userData.id, userData.username, userData.name, userData.lastname, userData.token);

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
    const user = new User(id, username, name, lastname, token);
    this.user.next(user);
    localStorage.setItem('userData', JSON.stringify(user));
  }
}
