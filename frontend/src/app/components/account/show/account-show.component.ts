import { Component, OnInit } from '@angular/core';
import { AccountService } from '../../../services/account.service';
import { Account } from '../../../domain/account/Account';

@Component({
  selector: 'app-show',
  templateUrl: './account-show.component.html',
  styleUrls: ['./account-show.component.css']
})
export class AccountShowComponent implements OnInit {

  private accounts: Account[] = [];

  constructor(private accountService: AccountService) {
  }

  get accountsSortedByName() {
    return this.accounts.sort((a, b) => a.accountName.localeCompare(b.accountName));
  }

  ngOnInit() {
    this.accountService.getOpenedAccounts().subscribe(
      account => {this.accounts.push(account); }
    );
  }

}
