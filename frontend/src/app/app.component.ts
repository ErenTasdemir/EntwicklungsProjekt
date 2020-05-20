import {Component, OnInit} from '@angular/core';
import {Router, RouterEvent} from '@angular/router';
import {AuthService} from './auth/auth.service';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'frontend';

  constructor(private authService: AuthService, private titleService: Title) {
    this.titleService.setTitle('ShopBoss');
  }

  ngOnInit(): void {
    this.authService.autoLogin();
  }
}
