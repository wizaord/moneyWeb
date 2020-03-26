import { Component, Input, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { AccountMonthStatistiques } from '../../../../../domain/statistiques/AccountStatistiques';

@Component({
  selector: 'app-situation-patrimoniale',
  templateUrl: './situation-patrimoniale.component.html',
  styleUrls: ['./situation-patrimoniale.component.css']
})
export class SituationPatrimonialeComponent implements OnInit {

  @Input() lastYearStats: Observable<AccountMonthStatistiques[]>;
  chartDatas: any[];

  constructor() { }

  ngOnInit() {
  }

}
