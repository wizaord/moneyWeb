import { Component, OnInit } from '@angular/core';
import { UserAccountDetails } from '../../../domain/user/UserAccountDetails';
import { UserDetails } from '../../../domain/user/UserDetails';

@Component({
  selector: 'app-account-create',
  templateUrl: './account-create.component.html',
  styleUrls: ['./account-create.component.css']
})
export class AccountCreateComponent implements OnInit {

  private accountInfo: UserAccountDetails;
  private loading: false;
  private error = '';
  private warning = '';


  constructor() {
  }

  ngOnInit() {
    this.accountInfo = new UserAccountDetails('', '', '');
    this.addUser();
  }

  onCreate() {
    console.log(JSON.stringify(this.accountInfo));
  }

  addUser() {
    this.accountInfo.addUser(new UserDetails('hello'));
  }

  removeUser(user: UserDetails) {
    if (this.accountInfo.users.length <= 1) {
      this.warning = 'At least one user must be specified';
      return;
    }
    this.accountInfo.removeUser(user);
  }

  removeWarning() {
    this.warning = '';
  }
}
