import { Component, Input, OnInit } from '@angular/core';
import { EMPTY, Observable } from 'rxjs';
import { AccountMonthStatistiques } from '../../../../../domain/statistiques/AccountStatistiques';

@Component({
  selector: 'app-situation-patrimoniale',
  templateUrl: './situation-patrimoniale.component.html',
  styleUrls: ['./situation-patrimoniale.component.css']
})
export class SituationPatrimonialeComponent implements OnInit {

  @Input() lastYearStats: Observable<AccountMonthStatistiques[]> = EMPTY;
  chartDatas: any[];

  constructor() { }

  ngOnInit() {

  }

}
