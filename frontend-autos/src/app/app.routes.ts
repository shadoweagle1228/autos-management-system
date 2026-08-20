import { Routes } from '@angular/router';
import { LoginComponent } from './features/login/login';
import { AutosComponent } from './features/autos/autos';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'autos', component: AutosComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' }
];