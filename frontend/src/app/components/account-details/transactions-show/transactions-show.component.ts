import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-transactions-show',
  templateUrl: './transactions-show.component.html',
  styleUrls: ['./transactions-show.component.css']
})
export class TransactionsShowComponent implements OnInit, OnChanges {

  @Input() transactions$: Observable<Transaction[]>;
  @Input() soldeInit: number;

  private currentSolde: number;

  constructor() {
  }

  ngOnInit() {
  }

  getSolde(amount: number): number {
    this.currentSolde = this.currentSolde + amount;
    return this.currentSolde;
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.currentSolde = this.soldeInit;
  }
}
