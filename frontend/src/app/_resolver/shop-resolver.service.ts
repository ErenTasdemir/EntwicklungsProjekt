import {Shop, ShopService} from '../_services/shop.service';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';
import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ShopResolverService implements Resolve<Shop[]> {

  constructor(private shopService: ShopService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Shop[]> {
    return this.shopService.getAllShops();
  }
}
