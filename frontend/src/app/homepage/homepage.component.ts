import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent implements OnInit {
  model = {
    left: true,
    middle: false,
    right: false
  };

  constructor() { }

  ngOnInit() {
  }

}
