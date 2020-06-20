import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';
import {Shop, ShopService} from '../_services/shop.service';
import {Observable} from 'rxjs';
import {AuthService} from '../auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class UserShopsResolverService implements Resolve<Shop[]>{

  constructor(private shopService: ShopService, private authService: AuthService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Shop[]> | Promise<Shop[]> | Shop[] {
    let isAuthenticated;
    this.authService.user.subscribe( user => {
      isAuthenticated = !!user;
    } );
    return isAuthenticated ? this.shopService.getShopsForUser() : null;
  }
}
