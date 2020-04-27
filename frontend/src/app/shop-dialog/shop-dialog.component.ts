import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Shop, ShopService} from '../_services/shop.service';

export interface ShopDialogComponentData {
  shop: Shop;
}

@Component({
  selector: 'app-shop-dialog',
  templateUrl: './shop-dialog.component.html',
  styleUrls: ['./shop-dialog.component.css']
})
export class ShopDialogComponent implements OnInit {
  shop: Shop;
  isEditing = false;
  defaultShopType: string;

  options = [
    'Baeckerei',
    'Textilien',
    'Spielwaren',
    'Lebensmittel'
  ];

  constructor(public dialogRef: MatDialogRef<ShopDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: ShopDialogComponentData,
              public shopService: ShopService ) { }

  ngOnInit(): void {
    this.shop = this.data.shop;
  }

  onEdit() {
    this.isEditing = true;
  }

  onAccept() {

  }

  onCancel() {

  }

  onDelete() {

  }
}
