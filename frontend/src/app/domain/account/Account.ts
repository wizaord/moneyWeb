export class Account {
  accountName: string;
  bankName: string;
  dateCreate: Date;
  isOpened: boolean;
  owners: string[];
  solde: number;

  constructor(accountName: string,
              bankName: string,
              openDate: Date,
              isOpened: boolean,
  ) {
    this.accountName = accountName;
    this.bankName = bankName;
    this.dateCreate = openDate;
    this.isOpened = isOpened;
    this.owners = [];
    this.solde = 0;
  }
}
