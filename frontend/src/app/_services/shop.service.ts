import {Inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';


@Injectable()
export class ShopService {
  constructor(private http: HttpClient) { }

  getAllShops(): Observable<Shop[]> {
    console.log('Ich bin im ShopService');
    return this.http.get<Shop[]>('http://localhost:8081/shops');
  }
}

export interface Shop {
  shopLocation: string;
  shopName: string;
  shopType: string;

}
