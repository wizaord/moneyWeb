import { Component, Input, OnInit } from '@angular/core';
import { filter, flatMap, map, toArray } from 'rxjs/operators';
import { StatistiquesService } from '../../../../../services/statistiques.service';

@Component({
  selector: 'app-situation-patrimoniale',
  templateUrl: './situation-patrimoniale.component.html',
  styleUrls: ['./situation-patrimoniale.component.css']
})
export class SituationPatrimonialeComponent implements OnInit {

  @Input() currentDate: Date;
  chartDatas: any[];
  colorScheme = {domain: ['#9CD27D']  };

  soldeTotal = 0;
  lastEvol = 0;
  lastYearSolde = 0;

  constructor(private statistiquesService: StatistiquesService) {
  }

  ngOnInit() {
    this.refreshDatas(this.currentDate);
  }

  refreshDatas(limitDate: Date) {
    let solde = 0;
    const lastYear = new Date(limitDate.getFullYear() - 1, limitDate.getMonth(), 1);
    const lastYearTime = lastYear.getTime();
    const currentYearTime = limitDate.getTime();

    this.statistiquesService.getMonthStatsAggregateByMonth(true).pipe(
      flatMap(t => t),
      map(account => {
        solde += account.revenus - account.depenses;
        return new SoldeMonth(account.getMonthTime(), solde);
      }),
      filter(account => account.monthTime >= lastYearTime),
      filter(account => account.monthTime <= currentYearTime),
      toArray()
    ).subscribe(
      accountMonthStats => {
        this.chartDatas = [...[]];
        if (accountMonthStats.length > 2) {
          this.soldeTotal = accountMonthStats[accountMonthStats.length - 1].solde;
          this.lastEvol = accountMonthStats[accountMonthStats.length - 1].solde - accountMonthStats[accountMonthStats.length - 2].solde;
          this.lastYearSolde = accountMonthStats[0].solde;
        }

        accountMonthStats.forEach(accountMonthStat => {
          const chartDatasCurrent = {
            name: new Date(accountMonthStat.monthTime),
            value: accountMonthStat.solde,
          };
          this.chartDatas = [...this.chartDatas, chartDatasCurrent];
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
