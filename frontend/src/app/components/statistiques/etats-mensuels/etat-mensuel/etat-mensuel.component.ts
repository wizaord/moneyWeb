import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { StatistiquesService } from '../../../../services/statistiques.service';
import { AccountMonthStatistiques, AccountStatistiques } from '../../../../domain/statistiques/AccountStatistiques';
import { SituationPatrimonialeComponent } from './situation-patrimoniale/situation-patrimoniale.component';

@Component({
  selector: 'app-etat-mensuel',
  templateUrl: './etat-mensuel.component.html',
  styleUrls: ['./etat-mensuel.component.css']
})
export class EtatMensuelComponent implements OnInit {

  currentMonth: Date = new Date();
  loading = false;

  @ViewChild(SituationPatrimonialeComponent, {static: false}) situationPatrimonialComponent;


  allStatistiquesWithoutInternalTransactions: AccountStatistiques[] = [];

  private currentMonthStatBehavior = new BehaviorSubject<AccountMonthStatistiques>(AccountMonthStatistiques.Empty());
  currentMonthStats$ = this.currentMonthStatBehavior.asObservable();
  private previousMonthStatBehavior = new BehaviorSubject<AccountMonthStatistiques>(AccountMonthStatistiques.Empty());
  previousMonthStats$ = this.previousMonthStatBehavior.asObservable();

  constructor(
    private route: ActivatedRoute,
    private statistiquesService: StatistiquesService) {
  }

  ngOnInit() {
    this.loading = true;
    this.route.queryParamMap.subscribe(params => {
      const dateTime = params.get('date');
      this.currentMonth.setTime(Number(dateTime));
    });
    this.statistiquesService.getStatistiquesAccounts()
      .subscribe(
        result => {
          result.forEach(it => this.allStatistiquesWithoutInternalTransactions.push(it));
          this.refreshStatsObservable();
          this.loading = false;
        }
      );
  }

  refreshStatsObservable() {
    const previousMonthStr = this.convertDateAsString(new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() - 1, 1));
    const currentMonthStr = this.convertDateAsString(new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth(), 1));

    this.currentMonthStatBehavior.next(this.getFlattenAccountMonthStatistiques()
      .filter(value => value.month === currentMonthStr)
      .reduce(this.statistiquesService.aggregateAccountMonthStatistiques(), AccountMonthStatistiques.Empty(currentMonthStr)));

    this.previousMonthStatBehavior.next(this.getFlattenAccountMonthStatistiques()
      .filter(value => value.month === previousMonthStr)
      .reduce(this.statistiquesService.aggregateAccountMonthStatistiques(), AccountMonthStatistiques.Empty(previousMonthStr)));

    if (this.situationPatrimonialComponent !== undefined) {
      this.situationPatrimonialComponent.refreshDatas();
    }
  }


  private convertDateAsString(date: Date) {
    const year = date.getFullYear();
    const month = date.getMonth() + 1;
    return `${year}-${month}`;
  }

  private getFlattenAccountMonthStatistiques(): AccountMonthStatistiques[] {
    return [].concat(...this.allStatistiquesWithoutInternalTransactions
      .map(accountStat => accountStat.monthStatistiques));
  }

  goPreviousMonth() {
    this.currentMonth = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() - 1, this.currentMonth.getDate());
    this.refreshStatsObservable();
  }

  goNextMonth() {
    this.currentMonth = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() + 1, this.currentMonth.getDate());
    this.refreshStatsObservable();
  }

}
