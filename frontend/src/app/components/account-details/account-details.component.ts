import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { TransactionsService } from '../../services/transactions.service';
import { Transaction } from '../../domain/account/Transaction';
import { DateService } from '../../services/date.service';
import { AccountService } from '../../services/account.service';
import { flatMap } from 'rxjs/operators';

@Component({
  selector: 'app-account-details',
  templateUrl: './account-details.component.html',
  styleUrls: ['./account-details.component.css']
})
export class AccountDetailsComponent implements OnInit {
  accountNameTitle: string;
  currentMonth: Date = new Date();
  accountTransactions: Transaction[] = [];
  loading = true;
  FilteringMode = FilteringMode;
  filteringMode: FilteringMode = FilteringMode.DATE;
  private accountTransactionsBehavior = new BehaviorSubject<Transaction[]>([]);

  constructor(
    private route: ActivatedRoute,
    private transactionsService: TransactionsService,
    private dateService: DateService,
    private accountService: AccountService
  ) {
  }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.accountNameTitle = params.get('accountName');
      this.accountService.getAccountByName(this.accountNameTitle).pipe(
        flatMap(account => this.transactionsService.getTransactionsByAccount(account))
      ).subscribe(
        transactions => {
          transactions.forEach(transaction => this.accountTransactions.push(transaction));
          this.refreshTransactionSoldes();
          this.calculateCurrentMonth();
          this.extractTransactionsBasedOnFilteringModeSelected();
          this.loading = false;
        }
      );
    });
  }

  goPreviousMonth() {
    this.currentMonth = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() - 1, this.currentMonth.getDate());
    this.extractTransactionsBasedOnFilteringModeSelected();
  }

  goNextMonth() {
    this.currentMonth = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() + 1, this.currentMonth.getDate());
    this.extractTransactionsBasedOnFilteringModeSelected();
  }

  get transactionsFiltered$(): Observable<Transaction[]> {
    return this.accountTransactionsBehavior.asObservable();
  }

  private extractTransactionsBasedOnFilteringModeSelected() {
    switch (this.filteringMode) {
      case FilteringMode.ALL:
        this.extractAllTransaction();
        break;
      case FilteringMode.CRITERIA:
        break;
      case FilteringMode.DATE:
        this.extractTransactionsBasedOnDate();
        break;
      case FilteringMode.NONPOINTE:
        this.extractTransactionNonPointe();
        break;
    }
  }

  private extractAllTransaction() {
    this.accountTransactionsBehavior.next(this.accountTransactions);
  }

  private extractTransactionNonPointe() {
    const transactionsNonPointe = this.accountTransactions.filter(transaction => transaction.isPointe === false);
    this.accountTransactionsBehavior.next(transactionsNonPointe);
  }

  private extractTransactionsBasedOnDate() {
    const beginDateTime = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth(), 1);
    const endDateTime = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() + 1, 1);
    const transactionForMonth = this.accountTransactions.filter(
      transaction => this.dateService.isDateBetweenMonth(transaction.dateCreation, beginDateTime, endDateTime));
    this.accountTransactionsBehavior.next(transactionForMonth);
  }

  private calculateCurrentMonth() {
    if (this.accountTransactions.length !== 0) {
      const mostRecentDate = this.accountTransactions[this.accountTransactions.length - 1].dateCreation;
      this.currentMonth = new Date(mostRecentDate);
    }
  }

  private refreshTransactionSoldes() {
    let initSolde = 0;
    this.accountTransactions
      .sort((a, b) => (a.dateCreation.getTime() < b.dateCreation.getTime()) ? -1 : 1)
      .map(transaction => {
        transaction.currentSolde = initSolde + transaction.amount;
        initSolde = transaction.currentSolde;
      });
  }

  transactionUpdated(transaction: Transaction) {
    const transactionToUpdate = this.accountTransactions.find(t => t.id === transaction.id);
    this.transactionsService.updateTransaction(transaction).subscribe(
      result => {
        Object.assign(transactionToUpdate, transaction);
        console.log('Transaction updated');
        this.refreshTransactionSoldes();
        this.extractTransactionsBasedOnFilteringModeSelected();
      });
  }

  transactionRemove(transaction: Transaction) {
    this.accountTransactions.splice(
      this.accountTransactions.indexOf(transaction), 1);
    this.refreshTransactionSoldes();
    this.extractTransactionsBasedOnFilteringModeSelected();

    this.transactionsService.removeTransaction(transaction).subscribe();
  }

  transactionCreate(transaction: Transaction) {
    transaction.accountName = this.accountNameTitle;
    this.transactionsService.createTransaction(transaction).subscribe(
      result => {
        this.accountTransactions.push(result);
        this.refreshTransactionSoldes();
        this.extractTransactionsBasedOnFilteringModeSelected();
      }
    );
  }

  switchFilteringMode(typeFilter: FilteringMode) {
    console.log('Switch to =>' + typeFilter);
    this.filteringMode = typeFilter;
    this.extractTransactionsBasedOnFilteringModeSelected();
  }
}

export enum FilteringMode {
  DATE, ALL, NONPOINTE, CRITERIA

}
