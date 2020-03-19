import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-etats-mensuels',
  templateUrl: './etats-mensuels.component.html',
  styleUrls: ['./etats-mensuels.component.css']
})
export class EtatsMensuelsComponent implements OnInit {

  private currentMonth: Date = new Date();
  dates: Date[] = [];

  constructor() { }

  ngOnInit() {
    let i: number;
    for (i = 0; i < 10; i++) {
      this.dates.push(this.currentMonth);
      this.currentMonth = this.getPreviousMonth(this.currentMonth);
    }
  }

  private getPreviousMonth(date: Date) {
    return new Date(date.getFullYear(), date.getMonth() - 1);
  }

}

