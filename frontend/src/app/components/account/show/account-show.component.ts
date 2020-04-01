import { Component, OnInit } from '@angular/core';
import { AccountService } from '../../../services/account.service';
import { Account } from '../../../domain/account/Account';
import { TransactionsService } from '../../../services/transactions.service';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { FamilyService } from '../../../services/family.service';
import { AccountOwner } from '../../../domain/user/AccountOwner';

@Component({
  selector: 'app-show',
  templateUrl: './account-show.component.html',
  styleUrls: ['./account-show.component.css']
})
export class AccountShowComponent implements OnInit {

  private accounts: AccountShowList[] = [];
  private accountsToShow: AccountShowList[] = [];

  familyMembers$: Observable<AccountOwner[]>;

  constructor(private accountService: AccountService,
              private transactionsService: TransactionsService,
              private familyService: FamilyService) {
  }

  ngOnInit() {

    this.familyMembers$ = this.familyService.getOwners();

    this.accountService.getOpenedAccounts()
      .pipe(
        map(account => new AccountShowList(account))
      )
      .subscribe(
      account => {
        this.accounts.push(account);
        this.accountsToShow.push(account);
        this.transactionsService.getTransactionsByAccount(account).subscribe(
          transactions => {
            account.nbNonChecked = transactions.filter(transaction => transaction.isPointe === false).length;
            account.nbAnomalies = transactions.filter(transaction => transaction.isValid() === false).length;
          });
      }
    );
  }

  getMemberAccountsSortedByName(owner: AccountOwner) {
    return this.accountsToShow
      .filter(account => account.owners.find(o => o === owner.name))
      .sort((a, b) => a.accountName.localeCompare(b.accountName));
  }

  getMemberAccountSolde(owner: AccountOwner) {
    return this.accountsToShow
      .filter(account => account.owners.find(o => o === owner.name))
      .map(account => account.solde)
      .reduce((acc, elt) => acc += elt, 0);
  }

  getTotalSolde() {
    return this.accountsToShow
      .map(account => account.solde)
      .reduce((acc, elt) => acc += elt, 0);
  }
}


class AccountShowList extends Account {
  nbNonChecked: number;
  nbAnomalies: number;

  constructor(account: Account) {
    super(account);
    this.nbNonChecked = 0;
    this.nbAnomalies = 0;
  }
}
