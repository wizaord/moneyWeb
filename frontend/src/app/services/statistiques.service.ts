import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs';
import {AuthenticationService} from './authentification/authentication.service';
import {HttpClient} from '@angular/common/http';
import {AccountMonthStatistiques, AccountStatistiques} from '../domain/statistiques/AccountStatistiques';
import {filter, flatMap, groupBy, map, mergeMap, reduce, toArray} from 'rxjs/operators';
import {AccountService} from './account.service';
import {TransactionsService} from './transactions.service';
import {TransactionReduceByDay} from '../domain/statistiques/TransactionReduceByDay';

@Injectable({
  providedIn: 'root'
})
export class StatistiquesService {
  API_URL = `${environment.apiUrl}/family`;

  constructor(
    private http: HttpClient,
    private authenticationService: AuthenticationService,
    private accountService: AccountService,
    private transactionsService: TransactionsService
  ) {
  }

  getStatistiquesAccounts(includeInternal: boolean = false, owner?: string): Observable<AccountStatistiques[]> {
    const familyName = this.authenticationService.currentUserValue.username;
    let apiUrl = `${this.API_URL}/${familyName}/statistiques/accounts/notinternal`;
    if (includeInternal) {
      apiUrl = `${this.API_URL}/${familyName}/statistiques/accounts`;
    }
    return this.http.get<AccountStatistiques[]>(apiUrl).pipe(
      map(accountsStat => accountsStat.map(it => new AccountStatistiques(it))),
      map(accounts => accounts.filter(acc => owner === undefined || acc.owners.find(o => o === owner) !== undefined)),
    );
  }

  getFlattenAccountMonthStatistiques(includeInternal: boolean = false, owner?: string): Observable<AccountMonthStatistiques[]> {
    return this.getStatistiquesAccounts(includeInternal, owner).pipe(
      flatMap(t => t),
      map(x => x.monthStatistiques),
    );
  }

  getMonthStatsAggregateByMonth(includeInternal: boolean = false, owner?: string): Observable<AccountMonthStatistiques[]> {
    return this.getFlattenAccountMonthStatistiques(includeInternal, owner).pipe(
      flatMap(t => t),
      groupBy(x => x.month),
      mergeMap(group => group.pipe(
        reduce(this.aggregateAccountMonthStatistiques(), AccountMonthStatistiques.Empty()))
      ),
      toArray(),
      map(accounts => accounts.sort((a, b) => (a.getMonthTime() < b.getMonthTime()) ? -1 : 1)),
    );
  }

  /**
   * Regroupe une liste de AccountMonthStatistiques en 1 seul AccountMonthStatistiques.
   * Les depenses et les revenus sont additionnés
   */
  aggregateAccountMonthStatistiques() {
    return (acc: AccountMonthStatistiques, elt: AccountMonthStatistiques): AccountMonthStatistiques => {
      acc.depenses += elt.depenses;
      acc.revenus += elt.revenus;
      acc.month = elt.month;
      return acc;
    };
  }


  /**
   * Recoit une liste de AccountMonthStatistiques.
   *
   * Permet de retourner une liste de AccountMonthStatistiques dont :
   *  - chaque AccountMonthStatistiques est unique pour un mois donnée
   *  - tous elements du même mois sont aggregés
   *
   */
  groupByAndAggregateByMonth = (acc: AccountMonthStatistiques[], elt: AccountMonthStatistiques): AccountMonthStatistiques[] => {
    const isExist = acc.find(x => x.month === elt.month) !== undefined;
    const eltIn = acc.find(x => x.month === elt.month) || elt;
    eltIn.month = elt.month;
    eltIn.revenus += elt.revenus;
    eltIn.depenses += elt.depenses;
    if (!isExist) {
      acc.push(eltIn);
    }
    console.log('ACC => ' + JSON.stringify(acc));
    return acc;
  }

  getTransactionsGroupByDay(filteredFamilyMember: string[] = null): Observable<TransactionReduceByDay[]> {
    return this.accountService.getAccounts().pipe(
      flatMap(a => a),
      filter(account => filteredFamilyMember === null || this.transactionCheckOwner(account.owners, filteredFamilyMember)),
      mergeMap(account => this.transactionsService.getFlattenTransaction(account)),
      groupBy(transaction => transaction.dateCreation.getTime()),
      mergeMap(group => group.pipe(
        map(transaction => new TransactionReduceByDay(transaction.dateCreation, transaction.amount)),
        reduce((acc, val) => {
          acc.amount += val.amount;
          acc.nbTransaction += 1;
          return acc;
        }))
      ),
      toArray(),
      map( t => t.sort((a, b) => (a.date.getTime() < b.date.getTime()) ? -1 : 1))
    );
  }

  private transactionCheckOwner(transactionOwners: string[], membersToCheck: string[]): boolean {
    let isPresent = false;
    transactionOwners.forEach(owner => {
      if (membersToCheck.includes(owner)) {
        isPresent = true;
        return true;
      }
    });
    return isPresent;
  }
}
