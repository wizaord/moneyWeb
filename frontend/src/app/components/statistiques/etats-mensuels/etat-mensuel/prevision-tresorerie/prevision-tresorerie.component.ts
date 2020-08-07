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
  chartDatas: any[] = [];
  colorScheme = {domain: ['#9CD27D']};

  nextYearEvol = 0;
  nextYearSolde = 0;
  nextYearDate;

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
      result => {
        const previousYearStats: SoldeMonth[] = result;
        const lastSolde = previousYearStats[previousYearStats.length - 1];
        let currentSolde = lastSolde.solde;
        let currentDate = new Date(lastSolde.monthTime);
        let lastYearSolde = 0;

        const seriesElts = [{
          name: currentDate,
          value: currentSolde,
        }];

        lastYearSolde = previousYearStats.shift().solde;
        previousYearStats.forEach(soldeMonth => {
          const date = new Date(currentDate.getTime());
          date.setMonth(date.getMonth() + 1);
          currentDate = date;

          this.nextYearDate = new Date(currentDate.getFullYear() + 1, currentDate.getMonth() + 1, 0);

          const evolSolde = soldeMonth.solde - lastYearSolde;
          lastYearSolde = soldeMonth.solde;
          currentSolde += evolSolde;
          this.nextYearSolde = currentSolde;
          const chartDatasCurrent = {
            name: date,
            value: currentSolde,
          };
          seriesElts.push(chartDatasCurrent);
        });
        const chartElt = {
          name: 'Trésorerie',
          series: seriesElts
        };
        this.chartDatas = [...[], chartElt];
        this.nextYearEvol = this.nextYearSolde - previousYearStats[previousYearStats.length - 1].solde;
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
