import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
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
export class TransactionsShowComponent implements OnInit, OnChanges {

  @Input() transactions$: Observable<Transaction[]>;
  @Input() soldeInit: number;

  private currentSolde: number;

  constructor(private modalService: NgbModal,
              private transactionsService: TransactionsService) {
  }

  ngOnInit() {
    // FIXME: Voir pk le amout quand il est affiché remonte beaucoup d'erreur
  }

  getSolde(amount: number): number {
    return this.currentSolde += amount;
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.currentSolde = this.soldeInit;
  }

  openTransactionEditDialog(transaction: Transaction) {
    const modalRef = this.modalService.open(TransactionShowComponent,
      {backdropClass: 'light-blue-backdrop', size: 'lg'});
    modalRef.componentInstance.transaction = Object.assign({}, transaction);

    from(modalRef.result)
      .subscribe(transactionResult => {
        if (transactionResult != null) {
          Object.assign(transaction, transactionResult);
          this.transactionsService.updateTransaction(transactionResult).subscribe(
            result => {
              console.log('Transaction updated');
            });
        }
      });
  }
}
