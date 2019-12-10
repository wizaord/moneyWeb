import {Component, OnInit} from '@angular/core';

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

  ngOnInit() {
  }

  onConnect() {
    console.log(this.diagnostic);
  }

  get diagnostic() { return JSON.stringify(this.loginInfo); }
}
