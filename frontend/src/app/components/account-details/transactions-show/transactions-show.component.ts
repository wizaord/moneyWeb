import { Component, EventEmitter, Input, Output } from '@angular/core';
import { from, Observable } from 'rxjs';
import { TransactionEditComponent } from './transaction-edit/transaction-edit.component';
import { Transaction } from '../../../domain/account/Transaction';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-transactions-show',
  templateUrl: './transactions-show.component.html',
  styleUrls: ['./transactions-show.component.css']
})
export class TransactionsShowComponent {

  @Input() transactions$: Observable<Transaction[]>;
  @Output() transactionUpdate = new EventEmitter<Transaction>();
  @Output() transactionRemove = new EventEmitter<Transaction>();

  constructor(private modalService: NgbModal) {
  }

  openTransactionEditDialog(transaction: Transaction) {
    const modalRef = this.modalService.open(TransactionEditComponent,
      {backdropClass: 'light-blue-backdrop', size: 'lg'});
    modalRef.componentInstance.transaction = Object.assign({}, transaction);

    from(modalRef.result)
      .subscribe(transactionResult => {
        if (transactionResult != null) {
          this.transactionUpdate.emit(transactionResult);
        }
      });
  }

  deleteTransaction(transaction: Transaction) {
    console.log('Delete transaction');
    this.transactionRemove.emit(transaction);
  }
}
