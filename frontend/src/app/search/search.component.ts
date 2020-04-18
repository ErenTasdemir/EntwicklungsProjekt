import {Component, EventEmitter, OnInit} from '@angular/core';
import {combineLatest, Subject} from 'rxjs';
import {Shop, ShopService} from '../_services/shop.service';
import {ActivatedRoute} from '@angular/router';
import {takeUntil} from 'rxjs/operators';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css'],
  providers: [ShopService]
})
export class SearchComponent implements OnInit {
  shops: Shop[] = [];

  sendSearch = new EventEmitter<string>();

  searchedShops: Shop[] = [];

  query = '';

  destroy$ = new Subject<void>();
  radiusInput: number;

  formatLabel(value: number) {
    if (value >= 1000) {
      return Math.round(value / 1000) + 'k';
    }

    return value;
  }

  constructor(private shopService: ShopService,
              private route: ActivatedRoute) {
  }


  ngOnInit(): void {
    console.log('Vor dem Laden der Shops');
    this.loadShops();
    console.log('Nach dem Laden der Shops');
    this.sendSearch.subscribe(query => {
      this.query = query;
      console.log(query);
      this.shopService.searchShops(this.query);
    });
  }

  loadShops(): void {
    combineLatest(this.route.data)
      .pipe(takeUntil(this.destroy$))
      .subscribe(data => {
        if (data[0].shops) {
          this.shops = data[0].shops;
        }
      });
  }

  replaceInUmlaute(str) {
    return str.replace('UE', 'Ü')
    .replace('AE', 'Ä')
    .replace('OE', 'Ö')
    .replace('ue', 'ü')
    .replace('ae', 'ä')
    .replace('oe', 'ö');
  }

  replaceFromUmlaute(str) {
    return str.replace('Ü', 'UE')
      .replace('Ä', 'AE')
      .replace('Ö', 'OE')
      .replace('ü', 'ue')
      .replace('ä', 'ae')
      .replace('ö', 'oe');
  }


  sendSearchRequest(query: HTMLInputElement, location: HTMLInputElement, radius: number) {
    console.log(location.value);
    if (radius == null) {
      radius = 0;
    }
    this.shopService.searchShopsByRadius(this.replaceFromUmlaute(query.value).toLowerCase(),
      this.replaceFromUmlaute(location.value).toLowerCase(), radius)
      .subscribe(data => {
      console.log(data);
      this.shops = data;
    });
  }
}
