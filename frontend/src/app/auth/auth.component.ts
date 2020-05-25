import {Component, OnInit} from '@angular/core';
import {NgForm} from '@angular/forms';
import {AuthResponseData, AuthService} from './auth.service';
import {Router} from '@angular/router';
import {Observable} from 'rxjs';

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css']
})
export class AuthComponent implements OnInit {

  isLoginMode = true;
  isLoading = false;
  error: string = null;

  constructor(private authService: AuthService, private router: Router) {
  }

  ngOnInit(): void {
  }

  onSwitchMode() {
    this.isLoginMode = !this.isLoginMode;
  }

  onSubmit(authForm: NgForm) {
    if (!authForm.valid) {
      return;
    }

    this.isLoading = true;

    let authObs: Observable<AuthResponseData>;

    if (this.isLoginMode) {
      authObs = this.authService.login(authForm.value.username, authForm.value.password);
    } else {
      authObs = this.authService.register(authForm.value.username, authForm.value.password, authForm.value.name, authForm.value.lastname);
    }

    authObs.subscribe(value => {
        this.isLoading = false;
        this.router.navigate(['./search-shop']);
      }, errorRes => {
        if (errorRes.error.message === 'Access Denied') {
          this.error = 'Benutzername/Passwort stimmen nicht überein!';
        }
        if (errorRes.error.message === 'Username already taken') {
          this.error = 'Benutzername wird schon verwendet!';
        } else {
          this.error = errorRes.error.message;
        }
        this.isLoading = false;
      }
    );
    authForm.reset();
  }
}
