import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { AuthenticationService } from './authentification/authentication.service';
import { map } from 'rxjs/operators';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class FileUploadService {
  API_URL = `${environment.apiUrl}/family`;

  constructor(private http: HttpClient,
              private authenticationService: AuthenticationService) {
  }

  uploadFile(accountName: string, fileToUpload: File): Observable<boolean> {
    const familyName = this.authenticationService.currentUserValue.username;
    const apiUrl = `${this.API_URL}/${familyName}/accounts/${accountName}/upload`;

    const formData: FormData = new FormData();
    formData.append('fileKey', fileToUpload, fileToUpload.name);
    return this.http
      .post(apiUrl, formData)
      .pipe(
        map(() => true),
      );
  }
}
