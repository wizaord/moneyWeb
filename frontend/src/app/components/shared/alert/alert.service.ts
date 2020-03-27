import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { NavigationStart, Router } from '@angular/router';
import { Alert } from './alert';


@Injectable()
export class AlertService {
  private subject = new Subject<Alert>();
  private keepAfterRouteChange = false;

  constructor(private router: Router) {
    // clear alert messages on route change unless 'keepAfterRouteChange' flag is true
    router.events.subscribe(event => {
      if (event instanceof NavigationStart) {
        if (this.keepAfterRouteChange) {
          // only keep for a single route change
          this.keepAfterRouteChange = false;
        } else {
          // clear alert messages
          this.clear();
        }
      }
    });
  }

  getAlert(): Observable<any> {
    return this.subject.asObservable();
  }

  success(message: string, isTemporary: boolean = true, keepAfterRouteChange = false) {
    this.alert('success', message, isTemporary, keepAfterRouteChange);
  }

  error(message: string, isTemporary: boolean = true, keepAfterRouteChange = false) {
    this.alert('danger', message, isTemporary, keepAfterRouteChange);
  }

  info(message: string, isTemporary: boolean = true, keepAfterRouteChange = false) {
    this.alert('info', message, isTemporary, keepAfterRouteChange);
  }

  warn(message: string, isTemporary: boolean = true, keepAfterRouteChange = false) {
    this.alert('warning', message, isTemporary, keepAfterRouteChange);
  }

  private alert(type: string, message: string, isTemporary: boolean = true, keepAfterRouteChange = false) {
    this.keepAfterRouteChange = keepAfterRouteChange;
    this.subject.next({type, message, isTemporary } as Alert);
  }

  clear() {
    // clear alerts
    this.subject.next();
  }
}
