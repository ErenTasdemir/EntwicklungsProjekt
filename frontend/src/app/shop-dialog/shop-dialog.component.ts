import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Shop, ShopService} from '../_services/shop.service';
import {NgModel} from '@angular/forms';

export interface ShopDialogComponentData {
  shop: Shop;
}

@Component({
  selector: 'app-shop-dialog',
  templateUrl: './shop-dialog.component.html',
  styleUrls: ['./shop-dialog.component.css'],
})
export class ShopDialogComponent implements OnInit {

  shop: Shop;
  isEditing = false;

  options = [
    'Baeckerei',
    'Textilien',
    'Spielwaren',
    'Lebensmittel'
  ];

  constructor(public dialogRef: MatDialogRef<ShopDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: ShopDialogComponentData,
              public shopService: ShopService) {
  }

  ngOnInit(): void {
    this.shop = this.data.shop;
  }

  onEdit() {
    this.isEditing = true;
  }

  onAccept(shopName: NgModel, shopType: NgModel, shopLocation: NgModel) {
    this.shop.shopName = shopName.value;
    this.shop.shopType = shopType.value;
    this.shop.shopLocation = shopLocation.value;
    console.log(this.shop.shopName, this.shop.shopType, this.shop.shopLocation);
    this.shopService.editShop(shopName.value, shopType.value, shopLocation.value, this.shop.shopId).subscribe(value => {
      console.log(value);
      this.dialogRef.close([this.shop, 'edit']);
    });
    }


  onDelete(shopId: string) {
    shopId = this.shop.shopId;
    this.shopService.deleteShop(shopId).subscribe(value => {}
    );
    this.dialogRef.close([this.shop, 'delete'] );
    console.log(shopId + ' is deleted');
  }

}
