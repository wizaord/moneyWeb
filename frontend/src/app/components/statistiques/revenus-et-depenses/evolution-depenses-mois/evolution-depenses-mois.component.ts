import { Component, OnInit, ViewChild } from '@angular/core';
import { EvolutionDepenseGraphComponent } from './evolution-depense-graph/evolution-depense-graph.component';

@Component({
  selector: 'app-evolution-depenses-mois',
  templateUrl: './evolution-depenses-mois.component.html',
  styleUrls: ['./evolution-depenses-mois.component.css']
})
export class EvolutionDepensesMoisComponent implements OnInit {

  private loading: boolean;
  currentMonth: Date;

  @ViewChild('appEvolutionDepenses', {static: false}) evolutionDepensesGraph: EvolutionDepenseGraphComponent;

  constructor() {
    this.loading = true;
    this.currentMonth = new Date();
  }

  ngOnInit() {
  }

  goPreviousMonth() {
    this.currentMonth = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() - 1, this.currentMonth.getDate());
    this.evolutionDepensesGraph.refreshData(this.currentMonth);
  }

  goNextMonth() {
    this.currentMonth = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() + 1, this.currentMonth.getDate());
    this.evolutionDepensesGraph.refreshData(this.currentMonth);
  }


}
