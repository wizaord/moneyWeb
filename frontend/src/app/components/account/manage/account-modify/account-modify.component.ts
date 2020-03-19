import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AccountService } from '../../../../services/account.service';
import { Account } from '../../../../domain/account/Account';
import { FamilyService } from '../../../../services/family.service';
import { AccountOwner } from '../../../../domain/user/AccountOwner';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { DateService } from '../../../../services/date.service';

@Component({
  selector: 'app-account-modify',
  templateUrl: './account-modify.component.html',
  styleUrls: ['./account-modify.component.css']
})
export class AccountModifyComponent implements OnInit {

  accountNameTitle: string;
  accountToEdit: Account;
  accountOwners: AccountOwner[] = [];
  accountDate: NgbDateStruct;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private accountService: AccountService,
    private familyService: FamilyService,
    private dateService: DateService) {
    this.accountToEdit = {
      accountName : '',
      dateCreate : new Date(),
      owners : [],
      bankName : '',
      solde : 0,
      isOpened : true
    };
  }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.accountNameTitle = params.get('accountName');
      this.accountService.getAccountByName(this.accountNameTitle).subscribe(account => {
        this.accountToEdit = account;
        this.accountDate = this.dateService.convertToNgDateStruct(account.dateCreate);
      });
    });
    this.familyService.getOwners().subscribe(owners => owners.forEach(owner => this.accountOwners.push(owner)));
  }

  onAccountModify() {
    this.accountToEdit.dateCreate = this.dateService.convertToDate(this.accountDate);
    this.accountService.updateAccountInfo(this.accountNameTitle, this.accountToEdit).subscribe(
      account => this.router.navigate(['/accountManage'])
    );
  }
}
