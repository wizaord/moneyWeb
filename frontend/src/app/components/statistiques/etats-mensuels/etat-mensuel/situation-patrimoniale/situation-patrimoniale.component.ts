import { Component, Input, OnInit } from '@angular/core';
import { AccountMonthStatistiques } from '../../../../../domain/statistiques/AccountStatistiques';
import { filter, flatMap, groupBy, map, mergeMap, reduce, toArray } from 'rxjs/operators';
import { StatistiquesService } from '../../../../../services/statistiques.service';

@Component({
  selector: 'app-situation-patrimoniale',
  templateUrl: './situation-patrimoniale.component.html',
  styleUrls: ['./situation-patrimoniale.component.css']
})
export class SituationPatrimonialeComponent implements OnInit {

  @Input() currentDate: Date;
  chartDatas: any[];
  loading = true;

  constructor(private statistiquesService: StatistiquesService) {
  }

  ngOnInit() {
    this.refreshDatas();
  }

  refreshDatas() {
    this.loading = true;
    let solde = 0;
    const lastYear = new Date(this.currentDate.getFullYear() - 1, this.currentDate.getMonth(), 1);
    const lastYearTime = lastYear.getTime();
    this.statistiquesService.getFlattenAccountMonthStatistiques(true).pipe(
      flatMap(t => t),
      groupBy(x => x.month),
      mergeMap(group => group.pipe(
        reduce(this.statistiquesService.aggregateAccountMonthStatistiques(), AccountMonthStatistiques.Empty()))
      ),
      toArray(),
      map(accounts => accounts.sort((a, b) => (a.getMonthTime() < b.getMonthTime()) ? -1 : 1)),
      flatMap(t => t),
      map(account => {
        solde += account.revenus - account.depenses;
        return new SoldeMonth(account.getMonthTime(), solde);
      }),
      filter(account => account.monthTime >= lastYearTime),
      toArray()
    ).subscribe(
      accountMonthStats => {
        this.chartDatas = [...[]];
        accountMonthStats.forEach(accountMonthStat => {
          const chartDatasCurrent = {
            name: new Date(accountMonthStat.monthTime),
            value: accountMonthStat.solde,
          };
          this.chartDatas = [...this.chartDatas, chartDatasCurrent];
          this.loading = false;
        });
      }
    );
  }

}

class SoldeMonth {
  monthTime: number;
  solde: number;

  constructor(month: number, solde: number) {
    this.monthTime = month;
    this.solde = solde;
  }
}
