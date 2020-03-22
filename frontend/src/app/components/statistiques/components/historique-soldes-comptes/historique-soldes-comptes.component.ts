import { Component, OnInit } from '@angular/core';
import { TransactionsService } from '../../../../services/transactions.service';
import { AccountService } from '../../../../services/account.service';
import { filter, flatMap, groupBy, map, mergeMap, reduce, toArray } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { AccountOwner } from '../../../../domain/user/AccountOwner';
import { FamilyService } from '../../../../services/family.service';
import * as shape from 'd3-shape';

@Component({
  selector: 'app-historique-soldes-comptes',
  templateUrl: './historique-soldes-comptes.component.html',
  styleUrls: ['./historique-soldes-comptes.component.css']
})
export class HistoriqueSoldesComptesComponent implements OnInit {

  soldes = [
    {
      name: 'Solde',
      series: []
    }
  ];

  curve: any = shape.curveBasis;

  private allGroupedTransactions: TransactionReduceByDay[];
  filterSelected: number;
  filters = [new Filter(0, 'Les 6 derniers mois'),
    new Filter(1, 'L\'année précédente'),
    new Filter(2, 'Les 2 années précédentes'),
    new Filter(3, 'Toutes les dates')];

  filterFamilyMemberSelected: string = null;
  filterFamilyMember$: Observable<AccountOwner[]>;
  loading: boolean;


  constructor(
    private accountService: AccountService,
    private transactionsService: TransactionsService,
    private familyService: FamilyService) {
  }

  ngOnInit() {
    this.loading = true;
    this.filterSelected = 0;
    this.filterFamilyMember$ = this.familyService.getOwners();
    this.refreshData();
  }

  dateTickFormatting(val: any): string {
    if (val instanceof Date) {
      return val.getFullYear() + '/' + (val.getMonth() + 1) + '/' + val.getDate();
    }
    return val;
  }

  refreshData() {
    this.loading = true;
    this.accountService.getAccounts().pipe(
      flatMap(a => a),
      flatMap(account => this.transactionsService.getFlattenTransaction(account)),
      filter(transaction => this.filterFamilyMemberSelected === null || transaction.owners.includes(this.filterFamilyMemberSelected)),
      groupBy(transaction => transaction.dateCreation.getTime()),
      mergeMap(group => group.pipe(
        map(transaction => new TransactionReduceByDay(transaction.dateCreation, transaction.amount)),
        reduce((acc, val) => {
          acc.amount += val.amount;
          acc.nbTransaction += 1;
          return acc;
        }))
      ),
      toArray()
    ).subscribe(transactionsGrouped => {
      let currentSolde = 0;
      this.allGroupedTransactions = transactionsGrouped
        .sort((a, b) => (a.date.getTime() < b.date.getTime()) ? -1 : 1)
        .map(t => {
          currentSolde += t.amount;
          t.solde = currentSolde;
          return t;
        });
      this.refreshViewDate();
      this.loading = false;
    });
  }

  refreshViewDate() {
    let limiteDate: Date = new Date(1900, 0, 0);
    const currentDate = new Date();
    switch (this.filterSelected) {
      case 0: limiteDate = new Date(currentDate.getFullYear(), currentDate.getMonth() - 6); break;
      case 1: limiteDate = new Date(currentDate.getFullYear(), currentDate.getMonth() - 12); break;
      case 2: limiteDate = new Date(currentDate.getFullYear(), currentDate.getMonth() - 24); break;
    }
    this.soldes[0].series.splice(0, this.soldes[0].series.length);
    this.allGroupedTransactions
      .filter(t => t.date.getTime() > limiteDate.getTime())
      .forEach(t => {
        this.soldes[0].series = [...this.soldes[0].series, ...[{name: t.date, value: t.solde}]];
      });
    this.soldes = [...this.soldes];
  }
}

export class Filter {
  id: number;
  desc: string;

  constructor(id: number, desc: string) {
    this.id = id;
    this.desc = desc;
  }

}

export class TransactionReduceByDay {
  date: Date;
  amount: number;
  nbTransaction: number;
  solde: number;

  constructor(date: Date, amount: number) {
    this.date = date;
    this.amount = amount;
    this.nbTransaction = 0;
    this.solde = 0;
  }
}
