import {Inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';


@Injectable()
export class ShopService {
  constructor(private http: HttpClient) { }

  getAllShops(): Observable<Shop[]> {
    return this.http.get<Shop[]>('http://localhost:8081/shops');
  }

  searchShops(query: string): Observable<Shop[]> {
    query = encodeURI(query.replace('&', ' '));
    return this.http.get<Shop[]>( `http://localhost:8081/shops/search?query=${query}`);
}

  searchShopsByRadius(query: string, location: string, radius: number): Observable<Shop[]> {
    query = encodeURI(query.replace('&', ' '));
    location = encodeURI(location.replace('&', ' '));
    return this.http.get<Shop[]>( `http://localhost:8081/shops/search/searchbyradius?query=${query}&location=${location}&radius=${radius}`);
  }

  addNewShop(shopName: string, shopType: string, shopLocation: string): Observable<Shop> {
    // shopName = encodeURI(shopName.replace('&', ' '));
    // shopType = encodeURI(shopType.replace('&', ' '));
    // shopLocation = encodeURI(shopLocation.replace('&', ' '));
    const body = {
      shopName,
      shopType,
      shopLocation
    };
    return this.http.post<Shop>('http://localhost:8081/shops/add', body);
  }
}

export interface Shop {
  shopId: string;
  shopLocation: string;
  shopName: string;
  shopType: string;

}
