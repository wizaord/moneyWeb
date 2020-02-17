
class Transaction {
  id: string;
  amount: number;
  userLibelle: string;
  bankLibelle: string;
  bankDetail: string;
  isPointe: boolean;
  dateCreation: Date;

  constructor(id: string, amount: number, userLibelle: string, bankLibelle: string,
              bankDetail: string, isPointe: boolean, dateCreation: Date) {
    this.id = id;
    this.amount = amount;
    this.userLibelle = userLibelle;
    this.bankLibelle = bankLibelle;
    this.bankDetail = bankDetail;
    this.isPointe = isPointe;
    this.dateCreation = dateCreation;
  }
}
