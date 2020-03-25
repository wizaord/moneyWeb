import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { StatistiquesService } from '../../../../services/statistiques.service';
import { AccountMonthStatistiques, AccountStatistiques } from '../../../../domain/statistiques/AccountStatistiques';

@Component({
    selector: 'app-etat-mensuel',
    templateUrl: './etat-mensuel.component.html',
    styleUrls: ['./etat-mensuel.component.css']
})
export class EtatMensuelComponent implements OnInit {

    currentMonth: Date = new Date();

    allStatistiques: AccountStatistiques[] = [];

    private currentMonthStatBehavior = new BehaviorSubject<AccountMonthStatistiques>(AccountMonthStatistiques.Empty());
    currentMonthStats$ = this.currentMonthStatBehavior.asObservable();
    private previousMonthStatBehavior = new BehaviorSubject<AccountMonthStatistiques>(AccountMonthStatistiques.Empty());
    previousMonthStats$ = this.previousMonthStatBehavior.asObservable();

    private lastYearStatBehavior = new BehaviorSubject<AccountMonthStatistiques[]>([]);
    lastYearStats$ = this.lastYearStatBehavior.asObservable();

    constructor(
        private route: ActivatedRoute,
        private statistiquesService: StatistiquesService) {
    }

    ngOnInit() {
        this.route.queryParamMap.subscribe(params => {
            const dateTime = params.get('date');
            this.currentMonth.setTime(Number(dateTime));
        });
        this.statistiquesService.getStatistiquesAccounts()
            .subscribe(
                result => {
                    result.forEach(it => this.allStatistiques.push(it));
                    this.refreshStatsObservable();
                }
            );
    }


    refreshStatsObservable() {
        const previousMonthStr = this.convertDateAsString(new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() - 1, 1));
        const currentMonthStr = this.convertDateAsString(new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth(), 1));

        this.currentMonthStatBehavior.next(this.getFlattenAccountMonthStatistiques()
            .filter(value => value.month === currentMonthStr)
            .reduce((acc, elt) => {
                acc.depenses += elt.depenses;
                acc.revenus += elt.revenus;
                return acc;
            }, new AccountMonthStatistiques(currentMonthStr, 0, 0)));

        this.previousMonthStatBehavior.next(this.getFlattenAccountMonthStatistiques()
            .filter(value => value.month === previousMonthStr)
            .reduce((acc, elt) => {
                acc.depenses += elt.depenses;
                acc.revenus += elt.revenus;
                return acc;
            }, new AccountMonthStatistiques(previousMonthStr, 0, 0)));

        const statGroupByMonth = this.getFlattenAccountMonthStatistiques()
            .filter(value => value.isInPreviousYear(this.currentMonth))
            .reduce((acc, elt) => {
                    const isExist = acc.find(x => x.month === elt.month) !== undefined;
                    const eltIn = acc.find(x => x.month === elt.month) || elt;
                    eltIn.month = elt.month;
                    eltIn.revenus += elt.revenus;
                    eltIn.depenses += elt.depenses;
                    if (! isExist) { acc.push(eltIn); }
                    return acc;
                }
                , []);
        this.lastYearStatBehavior.next(Array.from(statGroupByMonth.values()));
    }

    private convertDateAsString(date: Date) {
        const year = date.getFullYear();
        const month = date.getMonth() + 1;
        return `${year}-${month}`;
    }

    private getFlattenAccountMonthStatistiques(): AccountMonthStatistiques[] {
        return [].concat(...this.allStatistiques
            .map(accountStat => accountStat.monthStatistiques));
    }

    goPreviousMonth() {
        this.currentMonth = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() - 1, this.currentMonth.getDate());
        this.refreshStatsObservable();
    }

    goNextMonth() {
        this.currentMonth = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() + 1, this.currentMonth.getDate());
        this.refreshStatsObservable();
    }

}
