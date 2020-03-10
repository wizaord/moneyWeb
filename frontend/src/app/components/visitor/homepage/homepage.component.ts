import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../../../services/authentification/authentication.service';
import { ActivatedRoute, Router } from '@angular/router';
import { first } from 'rxjs/operators';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent implements OnInit {

  loginInfo = {
    login: '',
    password: ''
  };

  loading = false;
  error = '';
  private returnUrl: string;

  constructor(private authentificationService: AuthenticationService,
              private route: ActivatedRoute,
              private router: Router) {
    if (this.authentificationService.currentUserValue) {
      this.router.navigate(['/']);
    }
  }

  ngOnInit() {
    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams.returnUrl || '/';
  }

  onConnect() {
    console.log(this.diagnostic);

    this.loading = true;
    this.authentificationService.login(this.loginInfo.login, this.loginInfo.password)
      .pipe(first())
      .subscribe(
        data => {
          this.router.navigate([this.returnUrl]);
        },
        error => {
          console.log(error);
          this.error = error;
          this.loading = false;
        });
  }

  get diagnostic() {
    return JSON.stringify(this.loginInfo);
  }
}
