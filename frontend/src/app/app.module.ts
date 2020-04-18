import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { NavigationComponent } from './navigation/navigation.component';
import {AddComponent} from './add/add.component';
import {SearchComponent} from './search/search.component';
import { RouterModule} from '@angular/router';
import {HomeComponent} from './home/home.component';
import appRoutingModule from './app-routing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatTableModule} from '@angular/material/table';
import {ShopService} from './_services/shop.service';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';



@NgModule({
  declarations: [
    AppComponent,
    NavigationComponent,
    AddComponent,
    SearchComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(appRoutingModule),
    BrowserAnimationsModule,
    MatTableModule,
    HttpClientModule

  ],
  providers: [
    ShopService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
