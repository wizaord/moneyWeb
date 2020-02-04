import { Component, OnInit } from '@angular/core';
import { NgbDateParserFormatter, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { AccountService } from '../../../../services/account.service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { AccountOwner } from '../../../../domain/user/AccountOwner';
import { UserService } from '../../../../services/user.service';

@Component({
  selector: 'app-account-create',
  templateUrl: './account-create.component.html',
  styleUrls: ['./account-create.component.css']
})
export class AccountCreateComponent implements OnInit {
  private loading = false;
  private error = '';

  private accountName: string;
  private openDate: NgbDateStruct;
  private accountOwners: AccountOwner[] = [];
  private ownersSelected: string[];
  private bankName: string;

  constructor(private ngbDateParserFormatter: NgbDateParserFormatter,
              private router: Router,
              private accountService: AccountService,
              private userService: UserService) { }

  ngOnInit() {
    this.accountName = '';
    this.bankName = '';
    // this.userService.getOwners().subscribe(owners => owners.forEach(owner => this.accountOwners.push(owner)));
  }

  onAccountCreate() {
    console.log('Creation du compte ' + this.accountName + 'avec la date de debut ' + this.ngbDateParserFormatter.format(this.openDate));
    this.loading = true;
    this.error = '';

    this.accountService.createAccount(this.accountName, this.bankName,
                                      this.ngbDateParserFormatter.format(this.openDate), this.ownersSelected)
      .subscribe(
          account => this.router.navigate(['/accountShow'])
        , error => this.handleError(error));
  }

  private handleError(error: HttpErrorResponse) {
    this.error = 'Oups. Error occured with our servers. Please retry in few minutes or contact us';
    this.loading = false;
  }
}
