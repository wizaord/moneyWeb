import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { TransactionsService } from '../../services/transactions.service';

@Component({
  selector: 'app-account-details',
  templateUrl: './account-details.component.html',
  styleUrls: ['./account-details.component.css']
})
export class AccountDetailsComponent implements OnInit {
  private accountNameTitle: string;
  private currentMonth: Date = new Date();
  private accountTransactions: Transaction[] = [];
  private accountTransactionsBehavior = new BehaviorSubject<Transaction[]>([]);

  constructor(
    private route: ActivatedRoute,
    private transactionsService: TransactionsService
  ) {
  }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.accountNameTitle = params.get('accountName');
      this.transactionsService.getTransactions(this.accountNameTitle).subscribe(
        transactions => transactions.forEach(transaction => this.accountTransactions.push(transaction))
      );
      this.refreshBehaviorDatas();
    });
  }

  goPreviousMonth() {
    this.currentMonth = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() - 1, this.currentMonth.getDate(),
      this.currentMonth.getHours(), this.currentMonth.getMinutes(), this.currentMonth.getSeconds());
    this.refreshBehaviorDatas();
  }

  goNextMonth() {
    this.currentMonth = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() + 1, this.currentMonth.getDate(),
      this.currentMonth.getHours(), this.currentMonth.getMinutes(), this.currentMonth.getSeconds());
    this.refreshBehaviorDatas();
  }

  get currentMonthTransactions$(): Observable<Transaction[]> {
    return this.accountTransactionsBehavior.asObservable();
  }

  private refreshBehaviorDatas() {
    const beginDateTime = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth(), 1).getTime();
    const endDateTime = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() + 1, 1).getTime();
    const transactionForMonth = this.accountTransactions.filter(transaction => {
      const dateTransaction = new Date(transaction.dateCreation).getTime();
      return dateTransaction > beginDateTime && dateTransaction < endDateTime;
    });
    this.accountTransactionsBehavior.next(transactionForMonth);
  }

  getSoldeInit(): number {
    const beginDateTime = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth(), 1).getTime();
    return this.accountTransactions.filter(transaction => {
      const dateTransaction = new Date(transaction.dateCreation).getTime();
      return dateTransaction < beginDateTime;
    }).map(transaction => transaction.amount)
      .reduce((previousValue, currentValue) => previousValue + currentValue);
  }
}

