import { Component, Input, OnInit } from '@angular/core';
import { Transaction } from '../../../../domain/account/Transaction';
import { NgbActiveModal, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { Ventilation } from '../../../../domain/account/Ventilation';
import { DateService } from '../../../../services/date.service';
import { TransactionsService } from '../../../../services/transactions.service';

@Component({
  selector: 'app-transaction-edit',
  templateUrl: './transaction-edit.component.html',
  styleUrls: ['./transaction-edit.component.css']
})
export class TransactionEditComponent implements OnInit {

  @Input() transaction: Transaction;
  accountDate: NgbDateStruct;

  userLibellePropositions: string[] = [];
  userLibelleSearch: string;
  isLoadingResult = false;

  constructor(private dateService: DateService,
              public activeModal: NgbActiveModal,
              public transactionsService: TransactionsService) {
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

  onChangeSearch(val: string) {
    console.log('Valeur à rechercher => ' + val);
    this.isLoadingResult = true;
    this.transactionsService.getDistinctTransactionUserLibelle(this.transaction.accountName, val)
      .subscribe(userlibelles => {
        this.userLibellePropositions = [];
        userlibelles.filter((item, i, ar) => ar.indexOf(item) === i)
                    .forEach(userLibelle => {
                      this.userLibellePropositions.push(userLibelle);
                    });

        this.isLoadingResult = false;
      });
  }

}


