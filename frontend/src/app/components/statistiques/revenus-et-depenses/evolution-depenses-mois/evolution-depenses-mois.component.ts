import { Component, OnInit } from '@angular/core';
import { TransactionsService } from '../../../../services/transactions.service';
import { filter, groupBy, map, mergeAll, mergeMap, reduce, toArray } from 'rxjs/operators';
import { Transaction } from '../../../../domain/account/Transaction';
import { of } from 'rxjs';

@Component({
  selector: 'app-evolution-depenses-mois',
  templateUrl: './evolution-depenses-mois.component.html',
  styleUrls: ['./evolution-depenses-mois.component.css']
})
export class EvolutionDepensesMoisComponent implements OnInit {
  barChart: any[] = [];
  lineChartSeries: any[] = [];
  lineChartScheme = {
    name: 'coolthree',
    selectable: true,
    group: 'Ordinal',
    domain: ['#01579b', '#7aa3e5', '#a8385d', '#00bfa5']
  };

  comboBarScheme = {
    name: 'singleLightBlue',
    selectable: true,
    group: 'Ordinal',
    domain: ['#01579b']
  };
  private loading: boolean;
  private currentMonth: Date;
  // private transactionsReduceByDay: TransactionReduceByDay[];
  private transactions: Transaction[] = [];
  maxAmount = 0;


  constructor(private transactionsService: TransactionsService) {
    this.loading = true;
    this.currentMonth = new Date();
  }

  ngOnInit() {
    this.refreshData();
  }

  goPreviousMonth() {
    this.currentMonth = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() - 1, this.currentMonth.getDate());
    this.refreshData();
  }

  goNextMonth() {
    this.currentMonth = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() + 1, this.currentMonth.getDate());
    this.refreshData();
  }

  private refreshData() {
    const fourMonthBefore = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() - 4, 1);
    const nextMonthDayOne = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() + 1, 1);
    // init charts
    this.transactionsService.getAllTransactionsNonInternal().pipe(
      mergeAll(),
      filter(t => t.dateCreation.getTime() >= fourMonthBefore.getTime()),
      filter(t => t.dateCreation.getTime() <= nextMonthDayOne.getTime()),
      toArray()
    ).subscribe(
      transactions => {
        this.transactions = transactions;
        this.setMaxAmount();
        this.setRevenusCourbe();
        this.loadDepenses();
      }
    );
  }

  private setRevenusCourbe() {
    // calcule pour les trois derniers mois, le revenus moyen
    of(this.transactions).pipe(
      mergeAll(),
      filter(t => t.amount >= 0),
      groupBy(t => t.dateCreation.getMonth()),
      mergeMap(group => group.pipe(
        map(t => t.amount),
        reduce((acc, val) => acc += val, 0),
        map(t => new RevenuByMonth(group.key, t))
        ),
      ),
      toArray(),
    ).subscribe(result => {
      console.log(result);
      const moyenneRevenu = result.map(t => t.amount).reduce((acc, elt) => acc += elt, 0) / result.length;
      console.log('Calculate moyenne revenus =>' + moyenneRevenu);

      const lineseries = [];
      let currentDate = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth(), 1);
      while (currentDate.getMonth() === this.currentMonth.getMonth()) {
        lineseries.push({name: currentDate.getDate(), value: moyenneRevenu});
        currentDate = new Date(currentDate.getFullYear(), currentDate.getMonth(), currentDate.getDate() + 1);
      }
      this.lineChartSeries = [...[], {name: 'Limite', series: lineseries}];
      if (moyenneRevenu > this.maxAmount) { this.maxAmount = moyenneRevenu; }
    });
  }

  private setMaxAmount() {
    this.maxAmount = 0;
    let currentDate = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth(), 1);
    while (currentDate.getMonth() === this.currentMonth.getMonth()) {
      const depenses = Math.abs(this.getDepenses(currentDate));
      if (depenses > this.maxAmount) {
        this.maxAmount = depenses;
      }
      currentDate = new Date(currentDate.getFullYear(), currentDate.getMonth(), currentDate.getDate() + 1);
    }
    console.log('Max amount => ' + this.maxAmount);
  }

  private loadDepenses() {
    let currentDate = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth(), 1);
    const currentMonth = this.currentMonth.getMonth();
    // const lineseries = [];
    const barSeries = [];
    while (currentDate.getMonth() === currentMonth) {
      const depenses = Math.abs(this.getDepenses(currentDate));
      barSeries.push({name: currentDate.getDate(), value: depenses});
      // lineseries.push({name: currentDate.getDate(), value: 0});
      currentDate = new Date(currentDate.getFullYear(), currentDate.getMonth(), currentDate.getDate() + 1);
    }

    this.barChart = [...barSeries];
    // this.lineChartSeries = [...[], {name: 'Limite', series: lineseries}];
    console.log(JSON.stringify(this.barChart));
    // console.log(JSON.stringify(this.lineChartSeries));
  }

  private getDepenses(date: Date): number {
    const depenseDate = this.transactions.filter(t => t.dateCreation.getTime() === date.getTime())
      .filter(t => t.amount <= 0)
      .map(t => t.amount)
      .reduce((acc, elt) => acc += elt, 0);
    if (depenseDate === undefined) {
      return 0;
    }
    return depenseDate;
  }
}


class RevenuByMonth {
  month: number;
  amount: number;

  constructor(month: number, amount: number) {
    this.month = month;
    this.amount = amount;
  }
}
