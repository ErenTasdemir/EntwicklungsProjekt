import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthService} from '../auth/auth.service';
import {User} from '../auth/user.model';
import {take, takeUntil} from 'rxjs/operators';
import {Shop, ShopService} from '../_services/shop.service';
import {combineLatest, Subject} from 'rxjs';
import {ActivatedRoute} from '@angular/router';
import {MatDialog} from '@angular/material/dialog';
import {ShopDialogComponent} from '../shop-dialog/shop-dialog.component';
import {AddComponent} from '../add/add.component';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit, OnDestroy {

  user: User;
  shops: Shop[] = [];

  destroy$ = new Subject<void>();


  constructor(private authService: AuthService, private route: ActivatedRoute,
              public dialog: MatDialog, public shopService: ShopService) { }

  ngOnInit(): void {
    this.loadShopsOfUser();
    this.authService.user.pipe(take(1)).subscribe(user => this.user = user );
  }

  loadShopsOfUser(): void {
    combineLatest(this.route.data)
      .pipe(takeUntil(this.destroy$))
      .subscribe(data => {
        if (data[0].shops) {
          this.shops = data[0].shops;
        }
      });
  }

  openShopDialog(shop: Shop) {
    const dialogRef = this.dialog.open(ShopDialogComponent, {
      data: {
        shop,
        ownedByUser: true
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

  openAddDialog(): void {
    const dialogRef = this.dialog.open(AddComponent);
    dialogRef.afterClosed().pipe(takeUntil(this.destroy$)).subscribe(value => {
      if (value) {
        this.shops.push(value);
      }
    });
  }

  ngOnDestroy(): void {
    this.destroy$.unsubscribe();
  }

}
