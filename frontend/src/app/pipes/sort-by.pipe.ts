import { Pipe, PipeTransform } from '@angular/core';
import { Transaction } from '../domain/account/Transaction';

@Pipe({
  name: 'sortTransactionByDate'
})
export class SortTransactionByDatePipe implements PipeTransform {

  transform(value: Transaction[]): Transaction[] {
    return value.sort((a, b) => {
      if (a.dateCreation.getTime() < b.dateCreation.getTime()) {
        return -1;
      }
      return 1;
    });
  }

}
