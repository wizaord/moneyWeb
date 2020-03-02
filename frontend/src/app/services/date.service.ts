import { Injectable } from '@angular/core';
import { NgbDateParserFormatter, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';

@Injectable({
  providedIn: 'root'
})
export class DateService {

  constructor(
    private ngbDateParserFormatter: NgbDateParserFormatter) {
  }

  convertToNgDateStruct(date: Date): NgbDateStruct {
    return {
      year: date.getFullYear(),
      month: date.getMonth() + 1,
      day: date.getDate()
    };
  }

  convertToDate(date: NgbDateStruct): Date {
    return new Date(this.ngbDateParserFormatter.format(date));
  }

  isDateBetweenMonth(dateCreation: Date, beginDateTime: Date, endDateTime: Date): boolean {
    const dateCreationNg = this.convertToNgDateStruct(dateCreation);
    const dateBeginNg = this.convertToNgDateStruct(beginDateTime);
    const dateEndNg = this.convertToNgDateStruct(endDateTime);
    const dateCreationDay = dateCreationNg.year * 365 + dateCreationNg.month * 12;
    const dateBeginDay = dateBeginNg.year * 365 + dateBeginNg.month * 12;
    const dateEndDay = dateEndNg.year * 365 + dateEndNg.month * 12;
    return dateCreationDay >= dateBeginDay && dateCreationDay < dateEndDay;
  }
}
