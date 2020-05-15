import {Component, EventEmitter, Inject, OnDestroy, OnInit, Output} from '@angular/core';
import {NgModel} from '@angular/forms';
import {Shop, ShopService} from '../_services/shop.service';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {ShopDialogComponentData} from '../shop-dialog/shop-dialog.component';


export class ShopData implements Shop{
  shopName: string;
  shopType: string;
  shopLocation: string;
  shopId: string;
  shopImage ?: any;
}
@Component({
  selector: 'app-add',
  templateUrl: './add.component.html',
  styleUrls: ['./add.component.css']
})
export class AddComponent implements OnInit, OnDestroy {
  @Output() shopAdded = new EventEmitter<ShopData>();

  shop = new ShopData();
  image: any;

  options = [
    'Baeckerei',
    'Textilien',
    'Spielwaren',
    'Lebensmittel'
  ];

  constructor(public shopService: ShopService,
              public dialogRef: MatDialogRef<AddComponent>,
              @Inject(MAT_DIALOG_DATA) public data: ShopDialogComponentData) {
  }

  ngOnDestroy(): void {
    }

  ngOnInit(): void {
  }

  onSave(shopName: NgModel, shopType: NgModel, shopLocation: NgModel) {
    this.shop.shopName = shopName.value;
    this.shop.shopType = shopType.value;
    this.shop.shopLocation = shopLocation.value;
    this.shopService.addNewShop(shopName.value, shopType.value, shopLocation.value).subscribe(value => {});
    this.dialogRef.close(this.shop);
  }

}
