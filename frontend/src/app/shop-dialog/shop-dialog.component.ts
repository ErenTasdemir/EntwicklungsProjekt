import {Component, Inject, OnDestroy, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Shop, ShopService} from '../_services/shop.service';
import {NgForm} from '@angular/forms';
import {Subscription} from 'rxjs';
import {AuthService} from '../auth/auth.service';

export interface ShopDialogComponentData {
  shop: Shop;
  ownedByUser: boolean;
}

@Component({
  selector: 'app-shop-dialog',
  templateUrl: './shop-dialog.component.html',
  styleUrls: ['./shop-dialog.component.css'],
})
export class ShopDialogComponent implements OnInit, OnDestroy{

  shop: Shop;
  ownedByUser: boolean;
  isEditing = false;
  private userSub: Subscription;
  isAuthenticated = false;

  options = [
    'Baeckerei',
    'Textilien',
    'Spielwaren',
    'Lebensmittel'
  ];

  constructor(public dialogRef: MatDialogRef<ShopDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: ShopDialogComponentData,
              public shopService: ShopService, private authService: AuthService) {
  }

  ngOnInit(): void {
    this.shop = this.data.shop;
    this.ownedByUser = this.data.ownedByUser;
    this.userSub = this.authService.user.subscribe(user => {
      this.isAuthenticated = !!user;
    } );
  }

  onEdit() {
    this.isEditing = true;
  }

  onSubmitEdit(editForm: NgForm) {
    this.shop.shopName = editForm.value.shopName;
    this.shop.shopType = editForm.value.shopType;
    this.shop.shopLocation = editForm.value.shopLocation;
    this.shopService.editShop(this.shop.shopName, this.shop.shopType, this.shop.shopLocation, this.shop.shopId).subscribe(value => {
      this.dialogRef.close([this.shop, 'edit']);
    });
    }


  onDelete() {
    this.shopService.deleteShop(this.shop.shopId).subscribe(value => {this.shop = value}
    );
    this.dialogRef.close([this.shop, 'delete'] );
  }

  ngOnDestroy(): void {
    this.userSub.unsubscribe();
  }

}
