import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-component-user-main',
  templateUrl: './user-homepage.component.html',
  styleUrls: ['./user-homepage.component.css']
})
export class UserHomepageComponent implements OnInit {

  currentDate = new Date();

  constructor() { }

  ngOnInit() {
  }

}
