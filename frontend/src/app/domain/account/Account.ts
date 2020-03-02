export class Account {
  accountName: string;
  bankName: string;
  dateCreate: Date;
  isOpened: boolean;
  solde: number;
  owners: string[];

  constructor(a: any) {
    this.accountName = a.accountName;
    this.bankName = a.bankName;
    this.dateCreate = new Date(a.dateCreate);
    this.isOpened = a.isOpened;
    this.owners = a.owners;
    this.solde = a.solde;
  }
}
