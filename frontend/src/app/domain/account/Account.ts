export class Account {
  id: string;
  accountName: string;
  bankName: string;
  dateCreate: Date;
  isOpened: boolean;
  owners: string[];

  constructor(id: string,
              accountName: string,
              bankName: string,
              openDate: Date,
              isOpened: boolean,
  ) {
    this.id = id;
    this.accountName = accountName;
    this.bankName = bankName;
    this.dateCreate = openDate;
    this.isOpened = isOpened;
    this.owners = [];
  }
}
