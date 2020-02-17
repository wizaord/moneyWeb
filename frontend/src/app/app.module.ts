import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HomepageComponent } from './components/visitor/homepage/homepage.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatCardModule } from '@angular/material/card';
import { AngularFontAwesomeModule } from 'angular-font-awesome';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { ErrorInterceptor } from './services/authentification/error-interceptor';
import { JwtInterceptor } from './services/authentification/jwt-interceptor';
import { UserAccountCreateComponent } from './components/visitor/user-account-create/user-account-create.component';
import { UserComponent } from './components/visitor/user-account-create/user/user.component';
import { AccountShowComponent } from './components/account/show/account-show.component';
import { AccountManageComponent } from './components/account/manage/account-manage.component';
import { UserHomepageComponent } from './components/homepage/user-homepage.component';
import { AccountCreateComponent } from './components/account/manage/account-create/account-create.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { FamilyComponent } from './components/family/family/family.component';
import { FamilyCreateComponent } from './components/family/family-create/family-create.component';
import { UploadComponent } from './components/upload/upload.component';
import { StatistiquesComponent } from './components/statistiques/statistiques.component';

@NgModule({
  declarations: [
    AppComponent,
    HomepageComponent,
    UserAccountCreateComponent,
    UserComponent,
    UserComponent,
    AccountShowComponent,
    AccountManageComponent,
    UserHomepageComponent,
    AccountCreateComponent,
    FamilyComponent,
    FamilyCreateComponent,
    UploadComponent,
    StatistiquesComponent,
  ],
  imports: [
    NgbModule,
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    FormsModule,
    MatToolbarModule,
    MatCardModule,
    AngularFontAwesomeModule,
    HttpClientModule,
    MatFormFieldModule,
    MatSelectModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },

    // provider used to create fake backend
    // fakeBackendProvider
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
