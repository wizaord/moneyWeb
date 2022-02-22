import {Ventilation} from './Ventilation';


export class Transaction {
  id: string;
  amount: number;
  userLibelle: string;
  bankLibelle: string;
  bankDetail: string;
  isPointe: boolean;
  dateCreation: Date;
  accountName: string;
  ventilations: Ventilation[] = [];
  currentSolde = 0;
  owners: string[];

  static fromTransaction(t: any): Transaction {
    return new Transaction(t.id, t.amount, t.userLibelle, t.bankLibelle,
      t.bankDetail, t.isPointe, t.dateCreation, t.accountName, t.ventilations);
  }

  constructor(id: string, amount: number, userLibelle: string,
              bankLibelle: string, bankDetail: string, isPointe: boolean,
              dateCreation: Date, accountName: string, ventilations: Ventilation[]) {
    this.id = id;
    this.amount = amount;
    this.userLibelle = userLibelle;
    this.bankLibelle = bankLibelle;
    this.bankDetail = bankDetail;
    this.isPointe = isPointe;
    this.dateCreation = new Date(dateCreation);
    this.accountName = accountName;
    if (ventilations != null) {
      this.ventilations = ventilations.map(ventilation => new Ventilation(ventilation.amount, ventilation.categoryId));
    }
    this.owners = [];
  }

  isValid(): boolean {
    return this.ventilations.filter(v => v.categoryId === null || v.categoryId.length === 0).length === 0;
  }

  isBetween(previousDate: Date, afterDate: Date) {
    const previousDateTime = previousDate.getTime();
    const afterDateTime = afterDate.getTime();
    const eltDateTime = this.dateCreation.getTime();
    return (eltDateTime >= previousDateTime && eltDateTime <= afterDateTime);
  }
}
