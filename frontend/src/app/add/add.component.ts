import {Component, EventEmitter, Inject, OnDestroy, OnInit, Output} from '@angular/core';
import {Shop, ShopService} from '../_services/shop.service';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {ShopDialogComponentData} from '../shop-dialog/shop-dialog.component';
import {NgForm, NgModel} from '@angular/forms';


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

  onSave(addForm: NgForm) {
    this.shop.shopName = addForm.value.shopName;
    this.shop.shopType = addForm.value.shopType;
    this.shop.shopLocation = addForm.value.shopLocation;
    this.shopService.addNewShop(addForm.value.shopName, addForm.value.shopType, addForm.value.shopLocation)
      .subscribe(value => {this.shop = value;
                           this.dialogRef.close(this.shop);
      });
  }

}
