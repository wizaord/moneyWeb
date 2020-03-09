import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { TransactionsService } from '../../services/transactions.service';
import { Transaction } from '../../domain/account/Transaction';
import { DateService } from '../../services/date.service';

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
        private transactionsService: TransactionsService,
        private dateService: DateService
    ) {
    }

    ngOnInit() {
        this.route.paramMap.subscribe(params => {
            this.accountNameTitle = params.get('accountName');
            this.transactionsService.getTransactions(this.accountNameTitle).subscribe(
                transactions => {
                    transactions.forEach(transaction => this.accountTransactions.push(transaction));
                    this.refreshTransactionSoldes();
                    this.calculateCurrentMonth();
                    this.prepareObservableTransactionForCurrentMonth();
                    this.loading = false;
                }
            );
        });
    }

    goPreviousMonth() {
        this.currentMonth = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() - 1, this.currentMonth.getDate());
        this.prepareObservableTransactionForCurrentMonth();
    }

    goNextMonth() {
        this.currentMonth = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() + 1, this.currentMonth.getDate());
        this.prepareObservableTransactionForCurrentMonth();
    }

    get currentMonthTransactions$(): Observable<Transaction[]> {
        return this.accountTransactionsBehavior.asObservable();
    }

    private prepareObservableTransactionForCurrentMonth() {
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
                this.prepareObservableTransactionForCurrentMonth();
            });
    }

  transactionRemove(transaction: Transaction) {
    this.accountTransactions.splice(
      this.accountTransactions.indexOf(transaction), 1);
    this.refreshTransactionSoldes();
    this.prepareObservableTransactionForCurrentMonth();

    this.transactionsService.removeTransaction(transaction).subscribe();
  }
}

