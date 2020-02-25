export class Ventilation {
  amount: number;
  categoryId: string;

  constructor(amount: number, categoryId: string) {
    this.amount = amount;
    this.categoryId = categoryId;
  }
}

export class Transaction {
  id: string;
  amount: number;
  userLibelle: string;
  bankLibelle: string;
  bankDetail: string;
  isPointe: boolean;
  dateCreation: Date;
  accountName: string;
  ventilations: Ventilation[];

  constructor(id: string, amount: number, userLibelle: string, bankLibelle: string,
              bankDetail: string, isPointe: boolean, dateCreation: Date, accountName: string,
              ventilations: Ventilation[]) {
    this.id = id;
    this.amount = amount;
    this.userLibelle = userLibelle;
    this.bankLibelle = bankLibelle;
    this.bankDetail = bankDetail;
    this.isPointe = isPointe;
    this.dateCreation = dateCreation;
    this.accountName = accountName;
    this.ventilations = ventilations
  }
}
