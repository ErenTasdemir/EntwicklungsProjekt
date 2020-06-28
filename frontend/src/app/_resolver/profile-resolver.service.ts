import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';
import {Shop, ShopService} from '../_services/shop.service';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProfileResolverService implements Resolve<Shop[]>{

  constructor(private shopService: ShopService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Shop[]> | Promise<Shop[]> | Shop[] {
    return this.shopService.getShopsForUser();
  }
}
