import { Component, OnInit } from '@angular/core';
import {AuthService} from '../auth/auth.service';
import {User} from '../auth/user.model';
import {take} from 'rxjs/operators';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  user: User;

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
    this.authService.user.pipe(take(1)).subscribe(user => this.user = user );
  }

}
