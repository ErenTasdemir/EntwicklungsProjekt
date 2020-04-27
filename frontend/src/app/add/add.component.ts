import {Component, EventEmitter, OnInit, Output, ViewChild} from '@angular/core';
import {NgForm, NgModel} from '@angular/forms';
import {Shop, ShopService} from '../_services/shop.service';


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
  shops: ShopData[] = [];

  constructor(private shopService: ShopService) {
  }
  ngOnInit(): void {
  }

  onInsert(shopName: NgModel, shopType: NgModel, shopLocation: NgModel ){
    this.submitted = true;
    const shop = new ShopData();
    shop.shopName = shopName.value;
    shop.shopType = shopType.value;
    shop.shopLocation = shopLocation.value;
    this.signupForm.reset();
    this.shopAdded.emit(shop);
    }
}
