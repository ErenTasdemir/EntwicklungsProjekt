import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { SearchComponent } from './search/search.component';
import { AddComponent } from './add/add.component';
import {ShopResolverService} from './_resolver/shop-resolver.service';

const appRoutingModule: Routes = [
  { path: 'home',
    component: HomeComponent
  },
  {
    path: 'search-shop',
    component: SearchComponent,
    resolve: {
      shops: ShopResolverService
    }
  },
  { path: 'add-shop',
    component: AddComponent
  }
];
export default appRoutingModule;
