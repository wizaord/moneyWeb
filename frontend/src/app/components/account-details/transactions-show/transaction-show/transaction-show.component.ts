import { Component, Inject, Input, OnInit } from '@angular/core';
import { Transaction } from '../../../../domain/account/Transaction';
import { NgbActiveModal, NgbDateParserFormatter, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { Ventilation } from '../../../../domain/account/Ventilation';

@Component({
  selector: 'app-transaction-show',
  templateUrl: './transaction-show.component.html',
  styleUrls: ['./transaction-show.component.css']
})
export class TransactionShowComponent implements OnInit {

  @Input() transaction: Transaction;
  private accountDate: NgbDateStruct;

  constructor(
    private ngbDateParserFormatter: NgbDateParserFormatter,
    public activeModal: NgbActiveModal) {
  }

  ngOnInit() {
    this.accountDate = this.ngbDateParserFormatter.parse(this.transaction.dateCreation.toISOString());
  }

  updateTransaction() {
    this.transaction.dateCreation = new Date(this.ngbDateParserFormatter.format(this.accountDate));
    this.activeModal.close(this.transaction);
  }

  removeVentilation(ventilation: Ventilation) {
    this.transaction.ventilations.splice(
      this.transaction.ventilations.findIndex(v => ventilation === v), 1
    );
  }

  addNewVentilation() {
    this.transaction.ventilations.push(new Ventilation(0, '1'));
  }
}
