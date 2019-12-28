import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-account-create',
  templateUrl: './account-create.component.html',
  styleUrls: ['./account-create.component.css']
})
export class AccountCreateComponent implements OnInit {

  accountInfo: AccountDetails = {
    login: '',
    password: '',
    mail: '',
    users: [{
      username: ''
    }]
  };
  loading: false;
  private error = '';
  private warning = '';


  constructor() {
  }

  ngOnInit() {
  }

  onCreate() {
    console.log(JSON.stringify(this.accountInfo));
  }

  addUser() {
    const newUser: UserDetails = {
      username: ''
    };
    this.accountInfo.users.push(newUser);
  }

  removeUser(user: UserDetails) {
    if (this.accountInfo.users.length <= 1) {
      this.warning = 'At least one user must be specified';
      return;
    }
    const index = this.accountInfo.users.indexOf(user, 0);
    if (index > -1) {
      this.accountInfo.users.splice(index, 1);
    }
  }

  removeWarning() {
    this.warning = '';
  }
}
