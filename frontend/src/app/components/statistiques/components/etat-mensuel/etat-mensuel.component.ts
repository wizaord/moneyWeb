import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TransactionsService } from '../../../../services/transactions.service';
import { Transaction } from '../../../../domain/account/Transaction';
import { BehaviorSubject, EMPTY, Observable } from 'rxjs';

@Component({
  selector: 'app-etat-mensuel',
  templateUrl: './etat-mensuel.component.html',
  styleUrls: ['./etat-mensuel.component.css']
})
export class EtatMensuelComponent implements OnInit {

  currentMonth: Date = new Date();
  private transactions: Transaction[] = [];
  private transactionBehavior = new BehaviorSubject<Transaction[]>([])
  revenusDepenseTransactions$: Observable<Transaction[]> = EMPTY;


  constructor(
    private route: ActivatedRoute,
    private transactionsService: TransactionsService) {
  }

  ngOnInit() {
    this.route.queryParamMap.subscribe(params => {
      const dateTime = params.get('date');
      this.currentMonth.setTime(Number(dateTime));
    });
    this.revenusDepenseTransactions$ = this.transactionBehavior.asObservable();
    this.transactionsService.getAllTransactions().subscribe(transactions => {
        this.transactions = transactions;
        this.updateRevenuDepensesDatas();
      });
  }

  updateRevenuDepensesDatas() {
    this.transactionBehavior.next(this.transactions);
  }

  goPreviousMonth() {
    this.currentMonth = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() - 1, this.currentMonth.getDate());
    this.updateRevenuDepensesDatas();
  }

  goNextMonth() {
    this.currentMonth = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() + 1, this.currentMonth.getDate());
    this.updateRevenuDepensesDatas();
  }

}
