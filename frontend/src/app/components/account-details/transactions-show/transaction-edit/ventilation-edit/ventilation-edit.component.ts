import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Ventilation } from '../../../../../domain/account/Ventilation';
import { CategoriesService } from '../../../../../services/categories.service';
import { SubCategoryFamily } from '../../../../../domain/categories/SubCategoryFamily';

@Component({
  selector: 'app-ventilation-edit',
  templateUrl: './ventilation-edit.component.html',
  styleUrls: ['./ventilation-edit.component.css']
})
export class VentilationEditComponent implements OnInit {
  categorySelected: string;
  categories: SubCategoryFamily[];
  @Input() ventilation: Ventilation;
  @Output() ventilationRemoveEvent = new EventEmitter<Ventilation>();

  constructor(private categoriesService: CategoriesService
  ) { }

  ngOnInit() {
    this.categoriesService.getCategoriesFlatMapAsSubCategories()
      .subscribe(subCategories => this.categories = subCategories);
    this.refreshCategoryName();
  }

  removeVentilation(ventilation: Ventilation) {
    this.ventilationRemoveEvent.emit(ventilation);
  }

  categorySelectedChange(categoryId: number) {
    console.log('Selection de la category avec l id ' + categoryId);
    this.ventilation.categoryId = categoryId.toString();
  }

  refreshCategoryName() {
    this.categorySelected = this.ventilation.categoryId;
  }
}
