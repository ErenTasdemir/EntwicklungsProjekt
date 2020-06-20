import {Component, EventEmitter, OnDestroy, OnInit} from '@angular/core';
import {combineLatest, Subject, Subscription} from 'rxjs';
import {Shop, ShopService} from '../_services/shop.service';
import {ActivatedRoute} from '@angular/router';
import {takeUntil} from 'rxjs/operators';
import {AddComponent, ShopData} from '../add/add.component';
import {MatDialog} from '@angular/material/dialog';
import {ShopDialogComponent} from '../shop-dialog/shop-dialog.component';
import {AuthService} from '../auth/auth.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css'],
  providers: [ShopService]
})
export class SearchComponent implements OnInit, OnDestroy {
  shops: Shop[] = [];
  userShops: Shop[] = [];

  sendSearch = new EventEmitter<string>();
  query = '';

  destroy$ = new Subject<void>();
  radiusInput: number;
  private userSub: Subscription;
  isAuthenticated = false;

  message: string;

  formatLabel(value) {
    return value + 'km';
  }

  constructor(public shopService: ShopService,
              private route: ActivatedRoute,
              public dialog: MatDialog,
              private authService: AuthService) {
  }


  ngOnInit(): void {
    this.userSub = this.authService.user.subscribe(user => {
      this.isAuthenticated = !!user;
    } );
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
        if (data[0].userShops) {
          this.userShops = data[0].userShops;
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

  openAddDialog(): void {
    const dialogRef = this.dialog.open(AddComponent);
    dialogRef.afterClosed().pipe(takeUntil(this.destroy$)).subscribe(value => {
      if (value) {
        this.shops.push(value);
        this.userShops.push(value);
      }
    });
  }

  openShopDialog(shop: Shop) {
    const ownedByUser = this.checkIfShopIsOwnedByUser(shop.shopId);
    const dialogRef = this.dialog.open(ShopDialogComponent, {
      data: {
        shop,
        ownedByUser
      }
    });
    dialogRef.afterClosed().pipe(takeUntil(this.destroy$)).subscribe(value => {
      if (value) {
        const i = this.shops.indexOf(value[0]);
        if (value[1] === 'delete') {
          this.shops.splice(i, 1);
        }
        if (value[1] === 'edit') {
          this.shops.splice(i, 1, value[0]);
        }
      }
    });
  }

  checkIfShopIsOwnedByUser(shopId: string) {
    const foundShop = this.userShops.find(value => value.shopId === shopId);
    return !!foundShop;
  }

  ngOnDestroy(): void {
    this.destroy$.unsubscribe();
    this.userSub.unsubscribe();
  }

}
