import { Component, OnInit } from '@angular/core';
import { AccountService } from '../../../services/account.service';
import { Account } from '../../../domain/account/Account';
import { TransactionsService } from '../../../services/transactions.service';
import { count, filter, flatMap, map } from 'rxjs/operators';
import { Router } from '@angular/router';

@Component({
  selector: 'app-show',
  templateUrl: './account-show.component.html',
  styleUrls: ['./account-show.component.css']
})
export class AccountShowComponent implements OnInit {

  private accounts: AccountShowList[] = [];

  constructor(private accountService: AccountService,
              private transactionsService: TransactionsService,
              private router: Router) {
  }

  ngOnInit() {
    this.accountService.getOpenedAccounts()
      .pipe(
        map(account => new AccountShowList(account))
      )
      .subscribe(
      account => {
        this.accounts.push(account);
        this.transactionsService.getTransactions(account.accountName)
          .pipe(
            flatMap(transactions => transactions),
            filter(transaction => transaction.isPointe === false),
            count()
          )
          .subscribe(value => account.nbNonChecked = value);
      }
    );
  }

  get accountsSortedByName() {
    return this.accounts.sort((a, b) => a.accountName.localeCompare(b.accountName));
  }
}


class AccountShowList extends Account {
  nbNonChecked: number;

  constructor(account: Account) {
    super(account);
    this.nbNonChecked = 0;
  }
}
