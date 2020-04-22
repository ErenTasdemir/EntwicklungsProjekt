import {Component, EventEmitter, OnInit} from '@angular/core';
import {combineLatest, Subject} from 'rxjs';
import {Shop, ShopService} from '../_services/shop.service';
import {ActivatedRoute} from '@angular/router';
import {takeUntil} from 'rxjs/operators';
import {AddComponent, ShopData} from '../add/add.component';
import {MatDialog} from '@angular/material/dialog';

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

  formatLabel(value) {
    return value + 'km';
  }

  constructor(private shopService: ShopService,
              private route: ActivatedRoute,
              public dialog: MatDialog) {
  }


  ngOnInit(): void {
    this.loadShops();
    this.sendSearch.subscribe(query => {
      this.query = query;
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
    if (radius == null) {
      radius = 0;
    }
    this.shopService.searchShopsByRadius(this.replaceFromUmlaute(query.value).toLowerCase(),
      this.replaceFromUmlaute(location.value).toLowerCase(), radius)
      .subscribe(data => {
        this.shops = data;
      });
  }

  onShopAdded(shop: ShopData) {
    this.shopService.addNewShop(shop.shopName, shop.shopType, shop.shopLocation)
      .subscribe(data => {
        this.shops.push(data);
      });
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(AddComponent);
    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result : ${result}`);
    });
  }
}
