import { Component, Inject, Input, OnInit } from '@angular/core';
import { Transaction } from '../../../../domain/account/Transaction';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-transaction-show',
  templateUrl: './transaction-show.component.html',
  styleUrls: ['./transaction-show.component.css']
})
export class TransactionShowComponent implements OnInit {

  @Input() transaction: Transaction;

  constructor(public activeModal: NgbActiveModal) {
  }

  ngOnInit() {
  }

}
