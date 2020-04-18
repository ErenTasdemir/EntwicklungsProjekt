import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { SearchComponent } from './search/search.component';
import { AddComponent } from './add/add.component';

const appRoutes: Routes = [
  { path: 'home',
    component: HomeComponent
  },
  {
    path: 'search-shop',
    component: SearchComponent
  },
  { path: 'add-shop',
    component: AddComponent
  }
];
export default appRoutes;
