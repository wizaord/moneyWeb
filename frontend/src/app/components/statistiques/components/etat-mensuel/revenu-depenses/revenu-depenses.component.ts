import { Component, Input, OnInit } from '@angular/core';
import { EMPTY, Observable } from 'rxjs';
import { Transaction } from '../../../../../domain/account/Transaction';
import { filter, flatMap } from 'rxjs/operators';
import { TransactionsService } from '../../../../../services/transactions.service';

@Component({
  selector: 'app-revenu-depenses',
  templateUrl: './revenu-depenses.component.html',
  styleUrls: ['./revenu-depenses.component.css']
})
export class RevenuDepensesComponent implements OnInit {

  @Input() transactions: Observable<Transaction[]> = EMPTY;
  @Input() currentMonth: Date;
  previousMonth: Date;

  private currentMonthBegin: number;
  private currentMonthEnd: number;
  private previousMonthBegin: number;
  private previousMonthEnd: number;

  currentMonthTotalRevenus: number;
  currentMonthTotalDepenses$: Observable<number>;
  currentMonthEpargne$: Observable<number>;

  previousMonthTotalRevenus$: Observable<number>;
  previousMonthTotalDepenses$: Observable<number>;
  previousMonthEpargne$: Observable<number>;


  constructor(private transactionsService: TransactionsService) { }

  ngOnInit() {
    this.previousMonth = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() - 1);

    this.currentMonthBegin = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth(), 1).getTime();
    this.currentMonthEnd = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() + 1, 0, 23).getTime();

    this.previousMonthBegin = new Date(this.previousMonth.getFullYear(), this.previousMonth.getMonth(), 1).getTime();
    this.previousMonthEnd = new Date(this.previousMonth.getFullYear(), this.previousMonth.getMonth() + 1, 0, 23).getTime();

    this.transactionsService.getAllTransactions().pipe(
      // filter(t => t.amount >= 0),
      // tap(x => console.log('plop2 =>' + x)),
      // map(t => t.amount),
      // toArray()
    ).subscribe(results => {
      // ref
      // console.log('Subscribe');
      // const total = results.reduce((previousValue, currentValue) => previousValue1, 0);
      // console.log('total => ' + total);
      this.currentMonthTotalRevenus = 12;
    });
  }

  private getCurrentMonthObservableTransactions(): Observable<Transaction> {
    return this.transactions.pipe(
      flatMap(t => t),
      filter(t => t.dateCreation.getTime() > this.currentMonthBegin && t.dateCreation.getTime() < this.currentMonthEnd),
    );
  }

  private getPreviousMonthObservableTransactions(): Observable<Transaction> {
    return this.transactions.pipe(
      flatMap(t => t),
      filter(t => t.dateCreation.getTime() > this.previousMonthBegin && t.dateCreation.getTime() < this.previousMonthEnd),
    );
  }

}
