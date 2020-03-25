import { Pipe, PipeTransform } from '@angular/core';
import { MONTH_NAMES } from '../domain/EMonth';


@Pipe({
  name: 'dateNextMonthExtract'
})
export class DateNextMonthExtractPipe implements PipeTransform {

  transform(date: Date): string {
    const transformedDate = new Date(date.getFullYear(), date.getMonth() + 1);
    return `${transformedDate.getFullYear()} - ${MONTH_NAMES[transformedDate.getMonth()]}`;
  }
}

@Pipe({
  name: 'datePreviousMonthExtract'
})
export class DatePreviousMonthExtractPipe implements PipeTransform {

  transform(date: Date): string {
    const transformedDate = new Date(date.getFullYear(), date.getMonth() - 1);
    return `${transformedDate.getFullYear()} - ${MONTH_NAMES[transformedDate.getMonth()]}`;
  }
}

@Pipe({
  name: 'dateCurrentMonthExtract'
})
export class DateCurrentMonthExtractPipe implements PipeTransform {

  transform(date: Date): string {
    return `${date.getFullYear()} - ${MONTH_NAMES[date.getMonth()]}`;
  }
}
