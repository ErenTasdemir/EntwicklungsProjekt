import {Component, OnInit} from '@angular/core';
import {Title} from '@angular/platform-browser';
import {AuthService} from './auth/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  constructor(private titleService: Title, private authService: AuthService) {
    this.titleService.setTitle('Shop.de');
  }

  ngOnInit(): void {
    this.authService.autoLoginOrLogout();
  }
}
