import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HomepageComponent} from './homepage/homepage.component';
import {HomepageToolbarComponent} from './homepage/homepage-toolbar/homepage-toolbar.component';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatCardModule} from '@angular/material/card';
import {AngularFontAwesomeModule} from 'angular-font-awesome';
import { UserHomeComponent } from './login/user-home/user-home.component';

@NgModule({
  declarations: [
    AppComponent,
    HomepageComponent,
    HomepageToolbarComponent,
    UserHomeComponent
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
    AngularFontAwesomeModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
