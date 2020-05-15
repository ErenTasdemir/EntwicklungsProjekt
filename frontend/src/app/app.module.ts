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
import {MatSliderModule} from '@angular/material/slider';
import {FormsModule} from '@angular/forms';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import { AuthComponent } from './auth/auth.component';
import {LoadingSpinnerComponent} from './shared/loading-spinner/loading-spinner.component';
import {AuthInterceptorService} from './auth/auth-interceptor.service';
import { ProfileComponent } from './profile/profile.component';



@NgModule({
  declarations: [
    AppComponent,
    NavigationComponent,
    AddComponent,
    SearchComponent,
    HomeComponent,
    AuthComponent,
    LoadingSpinnerComponent,
    ProfileComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(appRoutingModule),
    BrowserAnimationsModule,
    MatTableModule,
    HttpClientModule,
    MatSliderModule,
    FormsModule,
    MatSlideToggleModule
  ],
  providers: [
    ShopService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptorService,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
