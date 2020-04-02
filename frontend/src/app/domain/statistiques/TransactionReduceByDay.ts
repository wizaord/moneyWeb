
export class TransactionReduceByDay {
  date: Date;
  amount: number;
  nbTransaction: number;
  solde: number;

  constructor(date: Date, amount: number) {
    this.date = date;
    this.amount = amount;
    this.nbTransaction = 0;
    this.solde = 0;
  }
}
