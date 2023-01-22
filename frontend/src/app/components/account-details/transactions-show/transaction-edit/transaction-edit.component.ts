import {Component, HostListener, Input, OnInit, QueryList, ViewChildren} from '@angular/core';
import {Transaction} from '../../../../domain/account/Transaction';
import {NgbActiveModal, NgbDateStruct, NgbTypeaheadSelectItemEvent} from '@ng-bootstrap/ng-bootstrap';
import {Ventilation} from '../../../../domain/account/Ventilation';
import {DateService} from '../../../../services/date.service';
import {TransactionsService} from '../../../../services/transactions.service';
import {Observable} from 'rxjs';
import {debounceTime, distinctUntilChanged, filter, map, switchMap, tap} from 'rxjs/operators';
import {VentilationEditComponent} from './ventilation-edit/ventilation-edit.component';
import {AccountService} from '../../../../services/account.service';
import {Account} from '../../../../domain/account/Account';

@Component({
  selector: 'app-transaction-edit',
  templateUrl: './transaction-edit.component.html',
  styleUrls: ['./transaction-edit.component.css']
})
export class TransactionEditComponent implements OnInit {

  @Input() transaction: Transaction;
  @ViewChildren(VentilationEditComponent) ventilationsComponents !: QueryList<VentilationEditComponent>;

  accountDate: NgbDateStruct;
  isLoadingResult = false;
  transactionTypeAHead: Transaction;

  private account: Account;
  private userLabelSelected = '';

  constructor(private dateService: DateService,
              public activeModal: NgbActiveModal,
              public transactionsService: TransactionsService,
              private accountService: AccountService) {
  }

  ngOnInit() {
    this.accountDate = this.dateService.convertToNgDateStruct(this.transaction.dateCreation);
    this.transactionTypeAHead = new Transaction('', 0.0, this.transaction.userLibelle, '', '', false, new Date(), '', []);
    this.accountService.getAccountByName(this.transaction.accountName).subscribe(
      result => this.account = result
    );
  }

  @HostListener('window:keyup', ['$event']) keyUp(e: KeyboardEvent) {
    console.log(e);
    if (e.key === 'Enter' && e.shiftKey === true) {
      this.updateTransaction();
    }
  }

  updateTransaction() {
    this.transaction.dateCreation = this.dateService.convertToDate(this.accountDate);
    if (this.transactionTypeAHead.userLibelle === undefined) {
      this.transaction.userLibelle = this.userLabelSelected;
    } else {
      this.transaction.userLibelle = this.transactionTypeAHead.userLibelle;
    }
    this.activeModal.close(this.transaction);
  }

  removeVentilation(ventilation: Ventilation) {
    this.transaction.ventilations.splice(
      this.transaction.ventilations.findIndex(v => ventilation === v), 1
    );
  }

  addNewVentilation() {
    const amountAllTransactions = this.transaction.ventilations.map(t => t.amount).reduce(
      (previousValue, currentValue) => previousValue + currentValue, 0
    );
    const newVentilation = new Ventilation(this.transaction.amount - amountAllTransactions, null);
    this.transaction.ventilations.push(newVentilation);
  }

  searchUserLibelle = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(600),
      distinctUntilChanged(),
      tap(() => this.isLoadingResult = true),
      filter(term => term.length >= 3),
      switchMap(term =>
        this.transactionsService.getMatchTransactionsBasedOnUserLibelle(this.account, term).pipe(
          map(transactions => transactions.slice(0, 10))
        )
      ),
      tap(() => this.isLoadingResult = false)
    )

  typeaheadFormatter = (x: Transaction) => x.userLibelle;

  updateTransactionWithTypeAHead($event: NgbTypeaheadSelectItemEvent) {
    const transactionSelected: Transaction = $event.item;
    this.transaction.isPointe = true;
    this.transaction.ventilations.forEach((ventilation, index) => {
      if (transactionSelected.ventilations.length >= index) {
        ventilation.categoryId = transactionSelected.ventilations[index].categoryId;
      }
    });
    this.ventilationsComponents.forEach(item => item.refreshCategoryName());
  }

  traceUserSelection($event: any) {
    this.userLabelSelected = $event;
  }

  ventilationSelected(ventilation: Ventilation) {
    this.transaction.isPointe = true;
  }
}


