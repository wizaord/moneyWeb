import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { from, Observable } from 'rxjs';
import { TransactionEditComponent } from './transaction-edit/transaction-edit.component';
import { Transaction } from '../../../domain/account/Transaction';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CategoriesService } from '../../../services/categories.service';
import { flatMap } from 'rxjs/operators';

@Component({
  selector: 'app-transactions-show',
  templateUrl: './transactions-show.component.html',
  styleUrls: ['./transactions-show.component.css']
})
export class TransactionsShowComponent implements OnInit {

  @Input() transactions$: Observable<Transaction[]>;
  @Output() transactionUpdate = new EventEmitter<Transaction>();
  @Output() transactionRemove = new EventEmitter<Transaction>();

  private categoriesMap: Map<string, string> = new Map<string, string>();

  constructor(private modalService: NgbModal,
              private categoriesService: CategoriesService) {
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
    console.log('Delete transaction');
    this.transactionRemove.emit(transaction);
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

}
