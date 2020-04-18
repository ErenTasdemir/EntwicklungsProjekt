import { Component, OnInit } from '@angular/core';
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

  destroy$ = new Subject<void>();

  constructor(private shopService: ShopService,
              private route: ActivatedRoute) {
  }


  ngOnInit(): void {
    console.log('Vor dem Laden der Shops');
    this.loadShops();
    console.log('Nach dem Laden der Shops');
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

  replaceUmlaute(str) {
    return str.replace('UE', 'Ü')
    .replace('AE', 'Ä')
    .replace('OE', 'Ö')
    .replace('ue', 'ü')
    .replace('ae', 'ä')
    .replace('oe', 'ö');

  }
}
