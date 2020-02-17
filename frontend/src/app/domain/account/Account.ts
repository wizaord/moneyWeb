export class Account {
  accountName: string;
  bankName: string;
  dateCreate: Date;
  isOpened: boolean;
  solde: number;
  owners: string[];

  constructor(accountName: string,
              bankName: string,
              openDate: Date,
              isOpened: boolean,
              solde: number,
  ) {
    this.accountName = accountName;
    this.bankName = bankName;
    this.dateCreate = openDate;
    this.isOpened = isOpened;
    this.owners = [];
    this.solde = solde;
  }
}
