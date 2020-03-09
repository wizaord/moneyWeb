import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { AuthenticationService } from './authentification/authentication.service';
import { environment } from '../../environments/environment';
import { AccountUpload } from '../domain/account/AccountUpload';

@Injectable({
  providedIn: 'root'
})
export class FileUploadService {
  API_URL = `${environment.apiUrl}/family`;

  constructor(private http: HttpClient,
              private authenticationService: AuthenticationService) {
  }

  uploadFile(accountName: string, fileToUpload: File): Observable<AccountUpload> {
    const familyName = this.authenticationService.currentUserValue.username;
    const apiUrl = `${this.API_URL}/${familyName}/accounts/${accountName}/upload`;

    const formData: FormData = new FormData();
    formData.append('file', fileToUpload, fileToUpload.name);
    return this.http.post<AccountUpload>(apiUrl, formData);
  }
}
