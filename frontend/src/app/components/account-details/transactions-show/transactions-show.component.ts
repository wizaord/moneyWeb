import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { TransactionsService } from '../../../services/transactions.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-transactions-show',
  templateUrl: './transactions-show.component.html',
  styleUrls: ['./transactions-show.component.css']
})
export class TransactionsShowComponent implements OnInit, OnChanges {

  @Input() dateBegin: Date;
  @Input() dateEnd: Date;
  @Input() accountName: string;

  private transactions: Transaction[] = [];
  private loading = false;

  constructor(
    private transactionsService: TransactionsService
  ) {
  }

  ngOnInit() {
    this.refreshTransactions();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.dateBegin.firstChange || changes.dateEnd.firstChange ) {
      return;
    }
    const dateBeginCurrent = changes.dateBegin.previousValue;
    const dateEndCurrent = changes.dateEnd.previousValue;
    if (dateBeginCurrent.getTime() === this.dateBegin.getTime()) {
      return;
    }
    if (dateEndCurrent.getTime() === this.dateEnd.getTime()) {
      return;
    }
    this.refreshTransactions();
  }

  private refreshTransactions() {
    this.transactions.splice(0, this.transactions.length);
    this.loading = true;
    this.transactionsService.getTransactionsByDate(this.accountName, this.dateBegin, this.dateEnd).subscribe(
      transaction => {
        console.log('Added');
        this.transactions.push(transaction);
        this.loading = false;
      }
    );
  }
}
