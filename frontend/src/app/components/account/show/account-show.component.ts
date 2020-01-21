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

  constructor(private accountService: AccountService) { }

  ngOnInit() {
    this.accountService.getOpenedAccounts().subscribe(
      accounts => accounts.forEach(account => this.accounts.push(account))
    );
  }

}
