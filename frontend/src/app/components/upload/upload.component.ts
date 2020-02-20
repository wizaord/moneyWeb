import { Component, OnInit } from '@angular/core';
import { FileUploadService } from '../../services/file-upload.service';
import { AccountService } from '../../services/account.service';
import { Observable } from 'rxjs';
import { Account } from '../../domain/account/Account';
import { toArray } from 'rxjs/operators';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent implements OnInit {
  fileToUpload: File = null;
  private loading: boolean;
  private openedAccounts$: Observable<Account[]>;
  accountSelected: string;

  constructor(
    private fileUploadService: FileUploadService,
    private accountService: AccountService
  ) {
  }

  ngOnInit() {
    this.loading = false;
    this.openedAccounts$ = this.accountService.getOpenedAccounts().pipe(toArray());
  }

  handleFileInput(files: FileList) {
    this.fileToUpload = files.item(0);
  }

  uploadFile() {
    this.loading = true;
    console.log('account selected ' + this.accountSelected);
    this.fileUploadService.uploadFile(this.accountSelected, this.fileToUpload).subscribe(
      result => {
        this.loading = false;
      }
    );
  }
}
