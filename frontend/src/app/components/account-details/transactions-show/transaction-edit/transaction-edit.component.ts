import { Component, Input, OnInit } from '@angular/core';
import { Transaction } from '../../../../domain/account/Transaction';
import { NgbActiveModal, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { Ventilation } from '../../../../domain/account/Ventilation';
import { DateService } from '../../../../services/date.service';

@Component({
  selector: 'app-transaction-edit',
  templateUrl: './transaction-edit.component.html',
  styleUrls: ['./transaction-edit.component.css']
})
export class TransactionEditComponent implements OnInit {

  @Input() transaction: Transaction;
  private accountDate: NgbDateStruct;

  constructor(private dateService: DateService,
              public activeModal: NgbActiveModal) {
  }

  ngOnInit() {
    this.accountDate = this.dateService.convertToNgDateStruct(this.transaction.dateCreation);

  }

  updateTransaction() {
    this.transaction.dateCreation = this.dateService.convertToDate(this.accountDate);
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
