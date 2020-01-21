import { Component, OnInit } from '@angular/core';
import { Account } from '../../../domain/account/Account';
import { AccountService } from '../../../services/account.service';

@Component({
  selector: 'app-manage',
  templateUrl: './account-manage.component.html',
  styleUrls: ['./account-manage.component.css']
})
export class AccountManageComponent implements OnInit {

  private accounts: Array<Account> = [];

  constructor(private accountService: AccountService) { }

  ngOnInit() {
    this.accountService.getAccounts().subscribe(
      accounts => accounts.forEach(account => this.accounts.push(account))
    );
  }

  get openedAccounts() {
    return this.accounts.filter(a => a.isOpened);
  }

  get closedAccounts() {
    return this.accounts.filter(a => ! a.isOpened);
  }

}
