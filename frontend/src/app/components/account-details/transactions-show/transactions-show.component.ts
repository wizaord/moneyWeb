import { Component, Input, OnInit } from '@angular/core';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-transactions-show',
  templateUrl: './transactions-show.component.html',
  styleUrls: ['./transactions-show.component.css']
})
export class TransactionsShowComponent implements OnInit {

  @Input() transactions$: Observable<Transaction[]>;
  @Input() soldeInit: number;

  private loading = false;

  constructor() {
  }

  ngOnInit() {
  }

  getSolde(amount: number): number {
    this.soldeInit = this.soldeInit + amount;
    return this.soldeInit;
  }
}
