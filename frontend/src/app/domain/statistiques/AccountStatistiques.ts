import { MONTH_NAMES } from '../EMonth';

export class AccountStatistiques {
  accountName: string;
  monthStatistiques: AccountMonthStatistiques[];
  owners: string[];

  constructor(t: any) {
    this.accountName = t.accountName;
    this.owners = t.owners;
    this.monthStatistiques = t.monthStatistiques.map(it => AccountMonthStatistiques.fromAccountMonthStatistiques(it));
  }
}

export class AccountMonthStatistiques {

  month: string;
  revenus: number;
  depenses: number;

  static Empty(month: string = ''): AccountMonthStatistiques {
    return new AccountMonthStatistiques(month, 0, 0);
  }

  static fromAccountMonthStatistiques(t: any) {
    return new AccountMonthStatistiques(t.month, t.revenus, t.depenses);
  }

  constructor(month: string, revenus: number, depenses: number) {
    this.month = month;
    this.revenus = revenus;
    this.depenses = depenses;
  }

  getMonth(): number {
    return Number(this.month.split('-')[1]);
  }

  getYear(): number {
    return Number(this.month.split('-')[0]);
  }

  getMonthAsLanguage() {
    return MONTH_NAMES[this.getMonth() - 1];
  }

  getMonthTime(): number {
    const eltDate = new Date(this.getYear(), this.getMonth(), 1);
    return eltDate.getTime();
  }

  isBetween(previousDate: Date, afterDate: Date) {
    const previousDateTime = previousDate.getTime();
    const afterDateTime = afterDate.getTime();
    const eltDateTime = this.getMonthTime();
    return (eltDateTime >= previousDateTime && eltDateTime <= afterDateTime);
  }
}

