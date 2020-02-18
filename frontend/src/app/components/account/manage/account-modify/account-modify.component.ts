import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AccountService } from '../../../../services/account.service';
import { Account } from '../../../../domain/account/Account';
import { FamilyService } from '../../../../services/family.service';
import { AccountOwner } from '../../../../domain/user/AccountOwner';
import { NgbDateParserFormatter, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { MatSelect } from '@angular/material/select';

@Component({
  selector: 'app-account-modify',
  templateUrl: './account-modify.component.html',
  styleUrls: ['./account-modify.component.css']
})
export class AccountModifyComponent implements OnInit {

  private accountNameTitle: string;
  private accountToEdit: Account;
  private accountOwners: AccountOwner[] = [];
  private accountDate: NgbDateStruct;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private accountService: AccountService,
    private familyService: FamilyService,
    private ngbDateParserFormatter: NgbDateParserFormatter) {
    this.accountToEdit = new Account('', '', new Date(), false, 0.0);
  }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.accountNameTitle = params.get('accountName');
      this.accountService.getAccountById(this.accountNameTitle).subscribe(account => {
        this.accountToEdit = account;
        console.log('Date du compte => ' + JSON.stringify(account.dateCreate));
        this.accountDate = this.ngbDateParserFormatter.parse(account.dateCreate.toString());
      });
    });
    this.familyService.getOwners().subscribe(owners => owners.forEach(owner => this.accountOwners.push(owner)));
  }

  onAccountModify() {
    this.accountToEdit.dateCreate = new Date(this.ngbDateParserFormatter.format(this.accountDate));
    console.log(JSON.stringify(this.accountToEdit));
    this.accountService.updateAccountInfo(this.accountNameTitle, this.accountToEdit).subscribe(
      account => this.router.navigate(['/accountManage'])
    );
  }

  selectIsValid(selectOwnerRef: MatSelect): boolean {
    return selectOwnerRef.value != null && selectOwnerRef.value.length !== 0;
  }
}
