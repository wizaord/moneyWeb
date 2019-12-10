import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {HomepageComponent} from './homepage/homepage.component';
import {AuthGuard} from './authentification/auth.guard';
import {UserHomeComponent} from './login/user-home/user-home.component';


const routes: Routes = [
  { path: '', component: UserHomeComponent, canActivate: [AuthGuard] },
  { path: 'login', component: HomepageComponent },

  // otherwise redirect to home
  { path: '**', redirectTo: '' }];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
