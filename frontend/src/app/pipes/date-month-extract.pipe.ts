import { Pipe, PipeTransform } from '@angular/core';

const monthNames = ['January', 'February', 'March', 'April', 'May', 'June',
                    'July', 'August', 'September', 'October', 'November', 'December'];


@Pipe({
  name: 'dateNextMonthExtract'
})
export class DateNextMonthExtractPipe implements PipeTransform {

  transform(date: Date): string {
    const transformedDate = new Date(date.getFullYear(), date.getMonth() + 1);
    return `${transformedDate.getFullYear()} - ${monthNames[transformedDate.getMonth()]}`;
  }
}

@Pipe({
  name: 'datePreviousMonthExtract'
})
export class DatePreviousMonthExtractPipe implements PipeTransform {

  transform(date: Date): string {
    const transformedDate = new Date(date.getFullYear(), date.getMonth() - 1);
    return `${transformedDate.getFullYear()} - ${monthNames[transformedDate.getMonth()]}`;
  }
}

@Pipe({
  name: 'dateCurrentMonthExtract'
})
export class DateCurrentMonthExtractPipe implements PipeTransform {

  transform(date: Date): string {
    return `${date.getFullYear()} - ${monthNames[date.getMonth()]}`;
  }
}
