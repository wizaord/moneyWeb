import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { TransactionsService } from '../../../services/transactions.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-transactions-show',
  templateUrl: './transactions-show.component.html',
  styleUrls: ['./transactions-show.component.css']
})
export class TransactionsShowComponent implements OnInit{

  @Input() transactions$: Observable<Transaction[]>;

  private loading = false;

  constructor(
  ) {
  }

  ngOnInit() {
  }

}
