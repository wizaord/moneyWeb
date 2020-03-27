import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { from, Observable } from 'rxjs';
import { TransactionEditComponent } from './transaction-edit/transaction-edit.component';
import { Transaction } from '../../../domain/account/Transaction';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CategoriesService } from '../../../services/categories.service';
import { flatMap } from 'rxjs/operators';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmationDialogComponent } from '../../shared/confirmation-dialog/confirmation-dialog.component';

@Component({
  selector: 'app-transactions-show',
  templateUrl: './transactions-show.component.html',
  styleUrls: ['./transactions-show.component.css']
})
export class TransactionsShowComponent implements OnInit {

  @Input() transactions$: Observable<Transaction[]>;
  @Input() accountsName: string[];
  @Output() transactionUpdate = new EventEmitter<Transaction>();
  @Output() transactionRemove = new EventEmitter<Transaction>();
  @Output() transactionCreate = new EventEmitter<Transaction>();

  private categoriesMap: Map<string, string> = new Map<string, string>();

  constructor(private modalService: NgbModal,
              private categoriesService: CategoriesService,
              public dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.categoriesService.getCategoriesFlatMapAsSubCategories().pipe(
      flatMap(x => x),
    ).subscribe(subCate => this.categoriesMap.set(subCate.id, subCate.name));
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
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      width: '350px',
      data: 'Do you confirm the deletion of this transaction ?'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        console.log('Delete transaction');
        this.transactionRemove.emit(transaction);
      }
    });
  }

  getCategoryName(transaction: Transaction): string {
    if (transaction.ventilations.length === 0) {
      return '';
    }
    if (transaction.ventilations.length > 1) {
      return 'Ventilation';
    }
    return this.categoriesMap.get(transaction.ventilations[0].categoryId);
  }

  pointTransaction(transaction: Transaction) {
    transaction.isPointe = true;
    this.transactionUpdate.emit(transaction);
  }

  createNewTransaction() {
    const modalRef = this.modalService.open(TransactionEditComponent,
      {backdropClass: 'light-blue-backdrop', size: 'lg'});
    modalRef.componentInstance.transaction = new Transaction(null, 0, '', '', '', false, new Date(), this.accountsName[0], []);

    from(modalRef.result).subscribe(
      transactionResult => {
        if (transactionResult != null) {
          this.transactionCreate.emit(transactionResult);
        }
      });
  }
}
