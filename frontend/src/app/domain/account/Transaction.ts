import { Ventilation } from './Ventilation';


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

  constructor(t: any) {
    this.id = t.id;
    this.amount = t.amount;
    this.userLibelle = t.userLibelle;
    this.bankLibelle = t.bankLibelle;
    this.bankDetail = t.bankDetail;
    this.isPointe = t.isPointe;
    this.dateCreation = new Date(t.dateCreation);
    this.accountName = t.accountName;
    this.ventilations = t.ventilations.map(ventilation => new Ventilation(ventilation.amount, ventilation.categoryId));
  }
}
