import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { NavigationComponent } from './navigation/navigation.component';
import {AddComponent} from './add/add.component';
import {SearchComponent} from './search/search.component';
import { RouterModule} from '@angular/router';
import {HomeComponent} from './home/home.component';
import appRoutes from './appRoutes';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatTableModule} from '@angular/material/table';


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
    RouterModule.forRoot(appRoutes),
    BrowserAnimationsModule,
    MatTableModule,

  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
