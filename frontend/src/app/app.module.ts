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
import { AccountModifyComponent } from './components/account/manage/account-modify/account-modify.component';
import { AccountDetailsComponent } from './components/account-details/account-details.component';
import { TransactionsShowComponent } from './components/account-details/transactions-show/transactions-show.component';
import { TransactionEditComponent } from './components/account-details/transactions-show/transaction-edit/transaction-edit.component';
import {
  DateCurrentMonthExtractPipe,
  DateNextMonthExtractPipe,
  DatePreviousMonthExtractPipe
} from './pipes/date-month-extract.pipe';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { SortTransactionByDatePipe } from './pipes/sort-by.pipe';
import { NgSelectModule } from '@ng-select/ng-select';
import { VentilationEditComponent } from './components/account-details/transactions-show/transaction-edit/ventilation-edit/ventilation-edit.component';
import { ConfirmationDialogComponent } from './components/shared/confirmation-dialog/confirmation-dialog.component';
import { EtatsMensuelsComponent } from './components/statistiques/etats-mensuels/etats-mensuels.component';
import { RevenusEtDepensesComponent } from './components/statistiques/revenus-et-depenses/revenus-et-depenses.component';
import { ActifsEtPassifsComponent } from './components/statistiques/actifs-et-passifs/actifs-et-passifs.component';
import { EtatsDeComparaisonComponent } from './components/statistiques/etats-de-comparaison/etats-de-comparaison.component';
import { HistoriqueSoldesComptesComponent } from './components/statistiques/actifs-et-passifs/historique-soldes-comptes/historique-soldes-comptes.component';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { EtatMensuelComponent } from './components/statistiques/etats-mensuels/etat-mensuel/etat-mensuel.component';
import { RevenuDepensesComponent } from './components/statistiques/etats-mensuels/etat-mensuel/revenu-depenses/revenu-depenses.component';
import { SituationPatrimonialeComponent } from './components/statistiques/etats-mensuels/etat-mensuel/situation-patrimoniale/situation-patrimoniale.component';
import { EvolutionRevenusDepensesComponent } from './components/statistiques/actifs-et-passifs/evolution-revenus-depenses/evolution-revenus-depenses.component';
import { AlertComponent } from './components/shared/alert/alert.component';
import { AlertService } from './components/shared/alert/alert.service';
import { MatExpansionModule } from '@angular/material/expansion';
import { PrevisionTresorerieComponent } from './components/statistiques/etats-mensuels/etat-mensuel/prevision-tresorerie/prevision-tresorerie.component';

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
    AccountModifyComponent,
    AccountDetailsComponent,
    TransactionsShowComponent,
    TransactionEditComponent,
    DatePreviousMonthExtractPipe,
    DateNextMonthExtractPipe,
    DateCurrentMonthExtractPipe,
    SortTransactionByDatePipe,
    VentilationEditComponent,
    ConfirmationDialogComponent,
    EtatsMensuelsComponent,
    RevenusEtDepensesComponent,
    ActifsEtPassifsComponent,
    EtatsDeComparaisonComponent,
    HistoriqueSoldesComptesComponent,
    EtatMensuelComponent,
    EvolutionRevenusDepensesComponent,
    RevenuDepensesComponent,
    SituationPatrimonialeComponent,
    AlertComponent,
    PrevisionTresorerieComponent

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
        MatSelectModule,
        MatProgressSpinnerModule,
        MatDialogModule,
        MatButtonModule,
        MatInputModule,
        NgSelectModule,
        NgxChartsModule,
        MatExpansionModule
    ],
  entryComponents: [
    TransactionEditComponent,
    ConfirmationDialogComponent
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true},

    // provider used to create fake backend
    // fakeBackendProvider
    AlertService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
