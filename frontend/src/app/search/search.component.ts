import {Component, EventEmitter, OnInit} from '@angular/core';
import {combineLatest, Subject} from 'rxjs';
import {Shop, ShopService} from '../_services/shop.service';
import {ActivatedRoute} from '@angular/router';
import {takeUntil} from 'rxjs/operators';
import {AddComponent, ShopData} from '../add/add.component';
import {MatDialog} from '@angular/material/dialog';
import {ShopDialogComponent} from '../shop-dialog/shop-dialog.component';

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

  message: string;
  imageName: string;
  retrievedImage: any;

  formatLabel(value) {
    return value + 'km';
  }

  constructor(public shopService: ShopService,
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




  sendSearchRequest(query: HTMLInputElement, location: HTMLInputElement, radius: number) {
    if (radius == null) {
      radius = 0;
    }
    this.shopService.searchShopsByRadius(this.shopService.replaceFromUmlaute(query.value).toLowerCase(),
      this.shopService.replaceFromUmlaute(location.value).toLowerCase(), radius)
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

  openAddDialog(): void {
    const dialogRef = this.dialog.open(AddComponent);
    dialogRef.afterClosed().subscribe(() => {
      if (dialogRef.componentInstance.shop != null){
        const shop = new ShopData();
        shop.shopName = dialogRef.componentInstance.shop.shopName;
        shop.shopLocation = dialogRef.componentInstance.shop.shopLocation;
        shop.shopType = dialogRef.componentInstance.shop.shopType;
        shop.shopId = dialogRef.componentInstance.shop.shopId;
        this.shops.push(shop);
      }
    });
  }

  openShopDialog(shop: Shop) {
    const dialogRef = this.dialog.open(ShopDialogComponent, {
      data: {
        shop
      }
    });
    dialogRef.afterClosed().subscribe(value => {
      if (value) {
        const i = this.shops.indexOf(value.shopId);
        this.shops.splice(i, 1, value);
      }
    });
  }
}
