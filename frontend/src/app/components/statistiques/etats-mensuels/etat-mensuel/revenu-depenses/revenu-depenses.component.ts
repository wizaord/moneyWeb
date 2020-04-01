import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Observable } from 'rxjs';
import { AccountMonthStatistiques } from '../../../../../domain/statistiques/AccountStatistiques';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-revenu-depenses',
  templateUrl: './revenu-depenses.component.html',
  styleUrls: ['./revenu-depenses.component.css']
})
export class RevenuDepensesComponent implements OnInit {

  @Input() currentMonthStat$: Observable<AccountMonthStatistiques>;
  @Input() previousMonthStat$: Observable<AccountMonthStatistiques>;
  @Output() previousMonthRequested = new EventEmitter();

  chartDatas: any[];
  chartDatasPrevious =
    {
      name: 'PreviousMonth',
      series: [{name: 'Revenus', value: 0}, {name: 'Depenses', value: 0}]
    };

  chartDatasCurrent =
    {
      name: 'CurrentMonth',
      series: [{name: 'Revenus', value: 0}, {name: 'Depenses', value: 0}]
    };

  epargneCurrentMonth$: Observable<number>;
  epargnePreviousMonth$: Observable<number>;

  constructor() {
  }

  ngOnInit() {
    this.currentMonthStat$.subscribe(
      result => {
        this.chartDatasCurrent = {
          name: result.month,
          series: [{name: 'Revenus', value: result.revenus}
            , {name: 'Depenses', value: result.depenses}]
        };
        this.chartDatas = [...[], this.chartDatasPrevious, this.chartDatasCurrent];
      }
    );
    this.previousMonthStat$.subscribe(
      result => {
        this.chartDatasPrevious = {
          name: result.month,
          series: [{name: 'Revenus', value: result.revenus}
            , {name: 'Depenses', value: result.depenses}]
        };
        this.chartDatas = [...[], this.chartDatasPrevious, this.chartDatasCurrent];
      }
    );

    this.epargneCurrentMonth$ = this.currentMonthStat$.pipe(map(x => x.revenus - x.depenses));
    this.epargnePreviousMonth$ = this.previousMonthStat$.pipe(map(x => x.revenus - x.depenses));
  }
}
