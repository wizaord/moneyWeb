import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { from, Observable } from 'rxjs';
import { TransactionShowComponent } from './transaction-show/transaction-show.component';
import { Transaction } from '../../../domain/account/Transaction';
import { TransactionsService } from '../../../services/transactions.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-transactions-show',
  templateUrl: './transactions-show.component.html',
  styleUrls: ['./transactions-show.component.css']
})
export class TransactionsShowComponent {

  @Input() transactions$: Observable<Transaction[]>;
  @Output() transactionUpdate = new EventEmitter<Transaction>();

  constructor(private modalService: NgbModal) {
  }

  openTransactionEditDialog(transaction: Transaction) {
    const modalRef = this.modalService.open(TransactionShowComponent,
      {backdropClass: 'light-blue-backdrop', size: 'lg'});
    modalRef.componentInstance.transaction = Object.assign({}, transaction);

    from(modalRef.result)
      .subscribe(transactionResult => {
        if (transactionResult != null) {
          this.transactionUpdate.emit(transactionResult);
        }
      });
  }

}
