import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomepageComponent } from './homepage/homepage.component';
import { AuthGuard } from './authentification/auth.guard';
import { UserHomeComponent } from './login/user-home/user-home.component';
import { AccountCreateComponent } from './account-create/account-create.component';


const routes: Routes = [
  { path: '', component: UserHomeComponent, canActivate: [AuthGuard] },
  { path: 'login', component: HomepageComponent },
  { path: 'accountCreate', component: AccountCreateComponent },

  // otherwise redirect to home
  { path: '**', redirectTo: '' }];

@NgModule({
  imports: [RouterModule.forRoot(routes, { enableTracing: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
