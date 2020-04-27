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

  selectedFile: File;

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

  onFileChanged(event) {
    this.selectedFile = event.target.files[0];
  }

  onUpload(shopId: string) {
    const uploadImageData = new FormData();
    uploadImageData.append('imageFile', this.selectedFile, this.selectedFile.name);
    this.shopService.saveImageToShop(shopId, uploadImageData).subscribe(response => {
      this.shop.shopImage = response.shopImage;
    });

  }
}
