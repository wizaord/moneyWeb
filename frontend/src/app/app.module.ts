import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HomepageComponent } from './homepage/homepage.component';
import { HomepageToolbarComponent } from './homepage/homepage-toolbar/homepage-toolbar.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatCardModule } from '@angular/material/card';
import { AngularFontAwesomeModule } from 'angular-font-awesome';
import { UserHomeComponent } from './login/user-home/user-home.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { ErrorInterceptor } from './authentification/error-interceptor';
import { JwtInterceptor } from './authentification/jwt-interceptor';
import { AccountCreateComponent } from './account-create/account-create.component';
import { UserComponent } from './account-create/user/user.component';

@NgModule({
  declarations: [
    AppComponent,
    HomepageComponent,
    HomepageToolbarComponent,
    UserHomeComponent,
    AccountCreateComponent,
    UserComponent,
    UserComponent,
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
    HttpClientModule
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
