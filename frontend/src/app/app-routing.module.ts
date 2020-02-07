import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomepageComponent } from './components/visitor/homepage/homepage.component';
import { AuthGuard } from './services/authentification/auth.guard';
import { UserAccountCreateComponent } from './components/visitor/user-account-create/user-account-create.component';
import { AccountManageComponent } from './components/account/manage/account-manage.component';
import { AccountShowComponent } from './components/account/show/account-show.component';
import { UserHomepageComponent } from './components/homepage/user-homepage.component';
import { AccountCreateComponent } from './components/account/manage/account-create/account-create.component';
import { FamilyComponent } from './components/family/family/family.component';
import { FamilyCreateComponent } from './components/family/family-create/family-create.component';

const routes: Routes = [
  { path: '', component: UserHomepageComponent, canActivate: [AuthGuard] },
  { path: 'login', component: HomepageComponent },
  { path: 'userAccountCreate', component: UserAccountCreateComponent },
  { path: 'accountShow', component: AccountShowComponent, canActivate: [AuthGuard] },
  { path: 'accountManage', component: AccountManageComponent, canActivate: [AuthGuard] },
  { path: 'accountCreate', component: AccountCreateComponent, canActivate: [AuthGuard] },
  { path: 'family/show', component: FamilyComponent, canActivate: [AuthGuard] },
  { path: 'family/create', component: FamilyCreateComponent, canActivate: [AuthGuard] },

  // otherwise redirect to home
  { path: '**', redirectTo: '' }];

@NgModule({
  imports: [RouterModule.forRoot(routes, { enableTracing: false })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
