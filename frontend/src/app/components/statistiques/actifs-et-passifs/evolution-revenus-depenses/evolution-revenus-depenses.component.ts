import { Component, OnInit } from '@angular/core';
import { AccountMonthStatistiques } from '../../../../domain/statistiques/AccountStatistiques';
import { StatistiquesService } from '../../../../services/statistiques.service';
import { Filter } from '../historique-soldes-comptes/historique-soldes-comptes.component';
import { Observable } from 'rxjs';
import { AccountOwner } from '../../../../domain/user/AccountOwner';
import { FamilyService } from '../../../../services/family.service';
import { filter, flatMap, groupBy, mergeMap, reduce, tap, toArray } from 'rxjs/operators';

@Component({
  selector: 'app-evolution-revenus-depenses',
  templateUrl: './evolution-revenus-depenses.component.html',
  styleUrls: ['./evolution-revenus-depenses.component.css']
})
export class EvolutionRevenusDepensesComponent implements OnInit {

  filterSelected: number;
  filters = [new Filter(0, 'Les 6 derniers mois'),
    new Filter(1, 'L\'année précédente'),
    new Filter(2, 'Les 2 années précédentes'),
    new Filter(3, 'Toutes les dates')];

  filterFamilyMemberSelected: string = null;
  filterFamilyMember$: Observable<AccountOwner[]>;
  loading: boolean;

  chartDatas: any[];

  constructor(
    private statistiquesService: StatistiquesService,
    private familyService: FamilyService
  ) {
  }

  ngOnInit() {
    this.filterSelected = 1;
    this.filterFamilyMember$ = this.familyService.getOwners().pipe(tap(o => o.push(new AccountOwner('all'))));
    this.filterFamilyMemberSelected = 'all';
    this.refreshStatsObservable();
  }

  private getSelectedDate(): Date {
    let limiteDate: Date = new Date(1900, 0, 0);
    const currentDate = new Date();
    switch (this.filterSelected) {
      case 0: limiteDate = new Date(currentDate.getFullYear(), currentDate.getMonth() - 6); break;
      case 1: limiteDate = new Date(currentDate.getFullYear(), currentDate.getMonth() - 12); break;
      case 2: limiteDate = new Date(currentDate.getFullYear(), currentDate.getMonth() - 24); break;
    }
    return limiteDate;
  }

  refreshStatsObservable() {
    this.loading = true;
    const currentDate = new Date();
    const selectedDate = this.getSelectedDate();
    let userSelected = this.filterFamilyMemberSelected;
    if (userSelected === 'all' ) userSelected = undefined;

    this.chartDatas = [...[]];
    this.statistiquesService.getFlattenAccountMonthStatistiques(false, userSelected)
      .pipe(
        flatMap(t => t),
        filter(x => x.isBetween(selectedDate, currentDate)),
        groupBy(x => x.month),
        mergeMap(group => group.pipe(
          reduce(this.statistiquesService.aggregateAccountMonthStatistiques(), AccountMonthStatistiques.Empty()))),
        toArray()
      ).subscribe(
      result => result.forEach(accountMonthStatistiques => {
        const chartElt = {
          name: accountMonthStatistiques.month,
          series: [{name: 'Revenus', value: accountMonthStatistiques.revenus}
            , {name: 'Depenses', value: accountMonthStatistiques.depenses}]
        };
        this.chartDatas = [...this.chartDatas, chartElt];
        this.loading = false;
      }));
  }

}
