import { Component, Input, OnInit } from '@angular/core';
import { StatistiquesService } from '../../../../../services/statistiques.service';
import { filter, flatMap, map, toArray } from 'rxjs/operators';

@Component({
  selector: 'app-prevision-tresorerie',
  templateUrl: './prevision-tresorerie.component.html',
  styleUrls: ['./prevision-tresorerie.component.css']
})
export class PrevisionTresorerieComponent implements OnInit {
  @Input() currentDate: Date;
  chartDatas: any[];
  colorScheme = {domain: ['#9CD27D']  };

  constructor(private statistiquesService: StatistiquesService) {

  }

  ngOnInit() {
    this.refreshDatas();
  }

  refreshDatas() {
    let solde = 0;
    const lastYear = new Date(this.currentDate.getFullYear() - 1, this.currentDate.getMonth(), 1);
    const lastYearTime = lastYear.getTime();
    const currentYearTime = this.currentDate.getTime();
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
      result => {
        const previousYearStats: SoldeMonth[] = result;
        const lastSolde = previousYearStats[previousYearStats.length - 1];
        let currentSolde = lastSolde.solde;
        let currentDate = new Date(lastSolde.monthTime);
        let lastYearSolde = 0;

        this.chartDatas = [...[]];
        this.chartDatas = [...[{
          name: currentDate,
          value: currentSolde,
        }]];

        lastYearSolde = previousYearStats.shift().solde;
        previousYearStats.forEach(soldeMonth => {
          const date = new Date(currentDate.getTime());
          date.setMonth(date.getMonth() + 1);
          currentDate = date;

          const evolSolde = soldeMonth.solde - lastYearSolde;
          lastYearSolde = soldeMonth.solde;
          console.log('Evol de ' + evolSolde);
          currentSolde += evolSolde;
          console.log('new current solde ' + currentSolde);

          const chartDatasCurrent = {
            name: date,
            value: currentSolde,
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
