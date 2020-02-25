import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { TransactionsService } from '../../services/transactions.service';
import { Transaction } from '../../domain/account/Transaction';

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
  public loading = true;

  constructor(
    private route: ActivatedRoute,
    private transactionsService: TransactionsService
  ) {
  }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.accountNameTitle = params.get('accountName');
      this.transactionsService.getTransactions(this.accountNameTitle).subscribe(
        transactions => {
          transactions.forEach(transaction => this.accountTransactions.push(transaction));
          this.calculateCurrentMonth();
          this.refreshBehaviorDatas();
          this.loading = false;
        }
      );
    });
  }

  goPreviousMonth() {
    this.currentMonth = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() - 1, this.currentMonth.getDate());
    this.refreshBehaviorDatas();
  }

  goNextMonth() {
    this.currentMonth = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() + 1, this.currentMonth.getDate());
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

  getSoldeInit() {
    const beginDateTime = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth(), 1).getTime();
    return this.accountTransactions.filter(transaction => {
      const dateTransaction = new Date(transaction.dateCreation).getTime();
      return dateTransaction < beginDateTime;
    }).map(transaction => transaction.amount)
      .reduce((previousValue, currentValue) => previousValue + currentValue, 0);
  }

  private calculateCurrentMonth() {
    if (this.accountTransactions.length !== 0) {
      const mostRecentDate = this.accountTransactions[this.accountTransactions.length - 1].dateCreation;
      this.currentMonth = new Date(mostRecentDate);
    }
  }
}

