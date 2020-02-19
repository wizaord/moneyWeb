import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-account-details',
  templateUrl: './account-details.component.html',
  styleUrls: ['./account-details.component.css']
})
export class AccountDetailsComponent implements OnInit {
  private accountNameTitle: string;
  private currentMonth: Date = new Date();

  constructor(
    private route: ActivatedRoute
  ) {
  }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.accountNameTitle = params.get('accountName');
    });
  }

  getBeginMonthDate(): Date {
    return new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth(), 1);
  }

  getEndMonthDate(): Date {
    return new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() + 1, 1);
  }

  goPreviousMonth() {
    this.currentMonth = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() - 1, this.currentMonth.getDate(),
                                this.currentMonth.getHours(), this.currentMonth.getMinutes(), this.currentMonth.getSeconds());
  }

  goNextMonth() {
    this.currentMonth = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() + 1, this.currentMonth.getDate(),
                                this.currentMonth.getHours(), this.currentMonth.getMinutes(), this.currentMonth.getSeconds());
  }
}

