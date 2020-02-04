import { Component, OnInit } from '@angular/core';
import { UserAccountDetails } from '../../../domain/user/UserAccountDetails';
import { AccountOwner } from '../../../domain/user/AccountOwner';
import { UserService } from '../../../services/user.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../../services/authentification/authentication.service';

@Component({
  selector: 'app-account-create',
  templateUrl: './user-account-create.component.html',
  styleUrls: ['./user-account-create.component.css']
})
export class UserAccountCreateComponent implements OnInit {

  private accountInfo: UserAccountDetails;
  private loading: boolean;
  private error = '';
  private warning = '';


  constructor(private userService: UserService,
              private authentificationService: AuthenticationService,
              private router: Router) {
  }

  ngOnInit() {
    this.authentificationService.logout();
    this.accountInfo = new UserAccountDetails('', '', '');
  }

  onCreate() {
    this.clearMessages();
    this.loading = true;
    this.userService.createUser(this.accountInfo)
        .subscribe(
          userAccountDetails => {
            this.authentificationService.login(userAccountDetails.login, userAccountDetails.password)
              .subscribe(user => this.router.navigate(['/']));
          }
          , error => this.handleError(error));
  }

  clearMessages() {
    this.warning = undefined;
    this.error = undefined;
  }

  private handleError(error: HttpErrorResponse) {
    if (error.status === 409) {
      this.warning = 'Name or Email already exist. Please specify another value.';
    }
    if (error.status === 406) {
      this.warning = 'Your datas are not valid.';
    }
    this.loading = false;
  }
}
