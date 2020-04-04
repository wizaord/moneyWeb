import { Component, Input, OnInit } from '@angular/core';
import { filter, first, groupBy, map, mergeAll, mergeMap, reduce, toArray } from 'rxjs/operators';
import { CategoriesService } from '../../../../../services/categories.service';
import { of } from 'rxjs';
import { Transaction } from '../../../../../domain/account/Transaction';
import { TransactionsService } from '../../../../../services/transactions.service';

@Component({
  selector: 'app-evolution-depense-graph',
  templateUrl: './evolution-depense-graph.component.html',
  styleUrls: ['./evolution-depense-graph.component.css']
})
export class EvolutionDepenseGraphComponent implements OnInit {

  private salaireNetId: string;
  private transactions: Transaction[] = [];
  private moyenneRevenu = 0;

  @Input() currentMonth: Date;

  barChart: any[] = [];
  lineChartSeries: any[] = [];
  maxAmount = 0;

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

  constructor(private categoriesService: CategoriesService,
              private transactionsService: TransactionsService) {
  }

  ngOnInit() {
    this.categoriesService.getCategoriesFlatMapAsSubCategories().pipe(
      mergeAll(),
      filter(cat => cat.name === 'Salaire net'),
      first()
    ).subscribe(salaireNetCate => {
      this.salaireNetId = salaireNetCate.id;
      this.transactionsService.getAllTransactionsNonInternal().subscribe(
        transactions => {
          this.transactions = transactions;
          this.refreshData(this.currentMonth);
        });
    });
  }

  public refreshData(currentMonth: Date) {
    // init charts
    this.currentMonth = currentMonth;
    this.lineChartSeries = [...[]];
    this.barChart = [...[]];
    this.setMaxAmount();
    this.setRevenusCourbe();
    this.setDepensesPrevues();
    this.loadDebitsAndCredits();
  }

  private setRevenusCourbe() {
    // calcule pour les trois derniers mois, le revenus moyen
    of(this.transactions).pipe(
      mergeAll(),
      filter(t => t.ventilations.find(v => v.categoryId === this.salaireNetId) !== undefined),
      filter(this.transactionIsLastMonth(4)),
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
      this.moyenneRevenu = result.map(t => t.amount).reduce((acc, elt) => acc += elt, 0) / result.length;
      console.log('Calculate moyenne revenus =>' + this.moyenneRevenu);

      const lineseries = [];
      let currentDate = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth(), 1);
      while (currentDate.getMonth() === this.currentMonth.getMonth()) {
        lineseries.push({name: currentDate.getDate(), value: this.moyenneRevenu.toFixed(2)});
        currentDate = new Date(currentDate.getFullYear(), currentDate.getMonth(), currentDate.getDate() + 1);
      }
      this.lineChartSeries = [...[], {name: 'Limite', series: lineseries}];
      if (this.moyenneRevenu > this.maxAmount) {
        this.maxAmount = this.moyenneRevenu;
      }
    });
  }

  private transactionIsLastMonth(nbPreviousMonth: number) {
    const currentMonth = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() + 1, 0);
    const lastXMonth = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() - nbPreviousMonth, 1);
    return (t: Transaction): boolean =>
      t.dateCreation.getTime() >= lastXMonth.getTime() && t.dateCreation.getTime() <= currentMonth.getTime()
  }

  private setMaxAmount() {
    this.maxAmount = 0;
    let currentDate = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth(), 1);
    while (currentDate.getMonth() === this.currentMonth.getMonth()) {
      const debits = Math.abs(this.getDebitsForDay(currentDate));
      if (debits > this.maxAmount) {
        this.maxAmount = debits;
      }
      currentDate = new Date(currentDate.getFullYear(), currentDate.getMonth(), currentDate.getDate() + 1);
    }
    console.log('Max amount => ' + this.maxAmount);
  }

  private loadDebitsAndCredits() {
    let currentDate = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth(), 1);
    const barSeries = [];
    const depensesLineSeries = [];
    let soldeDebutMois = this.moyenneRevenu;
    while (currentDate.getMonth() === this.currentMonth.getMonth()) {
      const debitsForDay = Math.abs(this.getDebitsForDay(currentDate));
      const creditsForDay = this.getCreditsForDay(currentDate);
      barSeries.push({name: currentDate.getDate(), value: debitsForDay.toFixed(2)});

      soldeDebutMois = ((soldeDebutMois - debitsForDay) + creditsForDay);
      depensesLineSeries.push({name: currentDate.getDate(), value: soldeDebutMois.toFixed(2)});

      currentDate = new Date(currentDate.getFullYear(), currentDate.getMonth(), currentDate.getDate() + 1);
    }

    this.barChart = [...barSeries];
    this.lineChartSeries = [...this.lineChartSeries, {name: 'Solde', series: depensesLineSeries}];
    // console.log(JSON.stringify(this.barChart));
    // console.log(JSON.stringify(this.lineChartSeries));
  }


  private setDepensesPrevues() {
    const expectedSoldeSeries = [];
    let currentDate = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth(), 1);
    let soldeDebutMois = this.moyenneRevenu;
    while (currentDate.getMonth() === this.currentMonth.getMonth()) {
      const previousMonthDay = new Date(currentDate.getFullYear(), currentDate.getMonth() - 1, currentDate.getDate());
      const previous2MonthDay = new Date(currentDate.getFullYear(), currentDate.getMonth() - 2, currentDate.getDate());
      const previous3MonthDay = new Date(currentDate.getFullYear(), currentDate.getMonth() - 3, currentDate.getDate());
      const debitsForDay = Math.abs(this.getDebitsForDay(previousMonthDay));
      const debits2ForDay = Math.abs(this.getDebitsForDay(previous2MonthDay));
      const debits3ForDay = Math.abs(this.getDebitsForDay(previous3MonthDay));
      soldeDebutMois = (soldeDebutMois - ((debitsForDay + debits2ForDay + debits3ForDay) / 3));
      expectedSoldeSeries.push({name: currentDate.getDate(), value: soldeDebutMois.toFixed(2)});
      currentDate = new Date(currentDate.getFullYear(), currentDate.getMonth(), currentDate.getDate() + 1);
    }
    this.lineChartSeries = [...this.lineChartSeries, {name: 'Previous Month', series: expectedSoldeSeries}];
  }

  private getDebitsForDay(date: Date): number {
    const debitValueExtracted = this.transactions.filter(t => t.dateCreation.getTime() === date.getTime())
      .filter(t => t.amount <= 0)
      .map(t => t.amount)
      .reduce((acc, elt) => acc += elt, 0);
    return (debitValueExtracted === undefined) ? 0 : debitValueExtracted;
  }

  private getCreditsForDay(date: Date) {
    const creditValueExtracted = this.transactions.filter(t => t.dateCreation.getTime() === date.getTime())
      .filter(t => t.amount >= 0)
      .filter(t => t.ventilations.find(v => v.categoryId === this.salaireNetId) === undefined)
      .map(t => t.amount)
      .reduce((acc, elt) => acc += elt, 0);
    return (creditValueExtracted === undefined) ? 0 : creditValueExtracted;
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
