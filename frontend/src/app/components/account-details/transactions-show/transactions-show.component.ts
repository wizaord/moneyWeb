import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-transactions-show',
  templateUrl: './transactions-show.component.html',
  styleUrls: ['./transactions-show.component.css']
})
export class TransactionsShowComponent implements OnInit {

  @Input() dateBegin: Date;
  @Input() dateEnd: Date;

  constructor() { }

  ngOnInit() {
    console.log('date begin => ' + this.dateBegin);
    console.log('date end => ' + this.dateEnd);
  }

}
