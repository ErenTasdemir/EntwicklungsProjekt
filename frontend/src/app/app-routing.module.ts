import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { SearchComponent } from './search/search.component';
import { AddComponent } from './add/add.component';
import {ShopResolverService} from './_resolver/shop-resolver.service';
import {AuthComponent} from './auth/auth.component';
import {ProfileComponent} from './profile/profile.component';
import {AuthGuard} from './auth/auth.guard';
import {ProfileResolverService} from './_resolver/profile-resolver.service';
import {UserShopsResolverService} from './_resolver/user-shops-resolver.service';

const appRoutingModule: Routes = [
  { path: '',
    component: HomeComponent
  },
  {
    path: 'search-shop',
    component: SearchComponent,
    resolve: {
      shops: ShopResolverService,
      userShops: UserShopsResolverService
    }
  },
  { path: 'add-shop',
    component: AddComponent
  },
  { path: 'auth',
    component: AuthComponent
  },
  {
    path: 'profile',
    canActivate: [AuthGuard],
    component: ProfileComponent,
    resolve: {
      shops: ProfileResolverService
    }
  }
];
export default appRoutingModule;
