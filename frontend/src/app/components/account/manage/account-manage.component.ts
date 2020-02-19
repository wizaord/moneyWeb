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

  closeAccount(account: Account) {
    console.log('Fermeture du compte ' + account.accountName);
    this.accountService.closAccount(account).subscribe(
      result => {
        account.isOpened = false;
      }
    );
  }

  openAccount(account: Account) {
    console.log('Fermeture du compte ' + account.accountName);
    this.accountService.openAccount(account).subscribe(
      result => {
        account.isOpened = true;
      }
    );
  }

  deleteAccount(account: Account) {
    this.accountService.deleteAccount(account).subscribe(
      result => {
        this.accounts.splice(this.accounts.indexOf(account), 1);
      }
    );
  }
}
