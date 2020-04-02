import { Component, OnInit } from '@angular/core';
import { StatistiquesService } from '../../../../services/statistiques.service';
import { TransactionReduceByDay } from '../../../../domain/statistiques/TransactionReduceByDay';

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
  private transactions: TransactionReduceByDay[];
  maxAmount = 0;

  constructor(private statistiquesService: StatistiquesService) {
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
    // init charts
    // get datas
    this.statistiquesService.getTransactionsGroupByDay().subscribe(
      transactionsGroupedByDay => {
        this.transactions = transactionsGroupedByDay;
        this.loadDepenses();
        // this.loadLimit();
        // this.loadPrevision();
        // set depense
      }
    );
  }

  private loadDepenses() {
    this.maxAmount = 0;
    let currentDate = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth(), 1);
    const currentMonth = this.currentMonth.getMonth();
    const lineseries = [];
    const barSeries = [];
    while (currentDate.getMonth() === currentMonth) {
      const depenses = Math.abs(this.getDepenses(currentDate));
      barSeries.push({name: currentDate, value: depenses});
      lineseries.push({name: currentDate, value: 100});
      currentDate = new Date(currentDate.getFullYear(), currentDate.getMonth(), currentDate.getDate() + 1);
      if (depenses > this.maxAmount) {this.maxAmount = depenses;}
    }

    this.barChart = [...barSeries];
    this.lineChartSeries = [...[], {name: 'Limite', series: lineseries}];
    console.log(JSON.stringify(this.barChart));
    console.log(JSON.stringify(this.lineChartSeries));
  }

  private getDepenses(date: Date): number {
    const transaction = this.getTransactionForDate(date);
    return (transaction === undefined) ? 0 : transaction.amount;
  }

  private getTransactionForDate(date: Date): TransactionReduceByDay | undefined {
    return this.transactions.find(t => t.date.getTime() === date.getTime());
  }
}
