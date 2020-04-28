import {Component, EventEmitter, Inject, OnInit, Output, ViewChild} from '@angular/core';
import {NgForm, NgModel} from '@angular/forms';
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
export class AddComponent implements OnInit {
  @Output() shopAdded = new EventEmitter<ShopData>();

  @ViewChild('f', {static: false}) signupForm: NgForm;

  submitted = false;
  shop = new ShopData();
  selectedFile: File;
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
  ngOnInit(): void {
  }

  onInsert(shopName: NgModel, shopType: NgModel, shopLocation: NgModel ){
    this.submitted = true;
    const shop = new ShopData();
    shop.shopName = shopName.value;
    shop.shopType = shopType.value;
    shop.shopLocation = shopLocation.value;
    this.shop = shop;
    this.signupForm.reset();
    this.shopAdded.emit(shop);
    }

  onFileChanged(event) {
    this.selectedFile = event.target.files[0];
    this.shop.shopImage = this.selectedFile;
  }

  onUpload(shopId: string): Shop {
    let shop = new ShopData();
    const uploadImageData = new FormData();
    uploadImageData.append('imageFile', this.selectedFile, this.selectedFile.name);
    this.shopService.saveImageToShop(shopId, uploadImageData).subscribe(response => {
      this.shop.shopImage = response.shopImage;
      shop = response;
    });
    return shop;
  }

  onSave(shopName: NgModel, shopType: NgModel, shopLocation: NgModel) {
    this.shop.shopName = shopName.value;
    this.shop.shopType = shopType.value;
    this.shop.shopLocation = shopLocation.value;
    this.shopService.addNewShop(shopName.value, shopType.value, shopLocation.value).subscribe(value => {
      this.shop = this.onUpload(value.shopId);
    });
    this.dialogRef.close(this.shop);
  }

}
