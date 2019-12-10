import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthentificationService {
  currentUserValue: false;

  constructor() {
  }
}
