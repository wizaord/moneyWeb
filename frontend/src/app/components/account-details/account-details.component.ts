import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BehaviorSubject, EMPTY, Observable } from 'rxjs';
import { TransactionsService } from '../../services/transactions.service';
import { Transaction } from '../../domain/account/Transaction';
import { DateService } from '../../services/date.service';
import { AccountService } from '../../services/account.service';
import { flatMap, toArray } from 'rxjs/operators';
import { Account } from '../../domain/account/Account';
import { AlertService } from '../shared/alert/alert.service';

@Component({
  selector: 'app-account-details',
  templateUrl: './account-details.component.html',
  styleUrls: ['./account-details.component.css']
})
export class AccountDetailsComponent implements OnInit {
  currentMonth: Date = new Date();
  accountTransactions: Transaction[] = [];
  loading = true;
  FilteringMode = FilteringMode;
  filteringMode: FilteringMode = FilteringMode.DATE;
  private accountTransactionsBehavior = new BehaviorSubject<Transaction[]>([]);
  private accountTransactions$ = this.accountTransactionsBehavior.asObservable();

  openedAccounts$: Observable<Account[]> = EMPTY;
  accountSelected: string[] = [];

  constructor(
    private route: ActivatedRoute,
    private transactionsService: TransactionsService,
    private dateService: DateService,
    private accountService: AccountService,
    private alertService: AlertService
  ) {
  }

  ngOnInit() {
    this.openedAccounts$ = this.accountService.getOpenedAccounts().pipe(toArray());

    this.route.paramMap.subscribe(params => {
      this.accountSelected.push(params.get('accountName'));
      this.refreshAccountSelection();
    });
  }

  private refreshAccountSelection() {
    this.loading = true;
    this.accountTransactions = [...[]];
    this.accountService.getAccountsByName(this.accountSelected).pipe(
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
  }

  goPreviousMonth() {
    this.currentMonth = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() - 1, this.currentMonth.getDate());
    this.extractTransactionsBasedOnFilteringModeSelected();
  }

  goNextMonth() {
    this.currentMonth = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() + 1, this.currentMonth.getDate());
    this.extractTransactionsBasedOnFilteringModeSelected();
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
    if (this.accountSelected.length !== 1) {
      this.alertService.error('Merci de selectionner un seul compte', true, false);
      return;
    }
    transaction.accountName = this.accountSelected[0];
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
