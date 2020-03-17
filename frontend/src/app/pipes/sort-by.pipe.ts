import { Pipe, PipeTransform } from '@angular/core';
import { Transaction } from '../domain/account/Transaction';

@Pipe({
  name: 'sortTransactionByDate'
})
export class SortTransactionByDatePipe implements PipeTransform {

  transform(value: Transaction[]): Transaction[] {
    return value.sort((a, b) => (a.dateCreation.getTime() < b.dateCreation.getTime()) ? -1 : 1);
  }

}
