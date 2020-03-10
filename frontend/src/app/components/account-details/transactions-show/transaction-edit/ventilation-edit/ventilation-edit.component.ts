import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Ventilation } from '../../../../../domain/account/Ventilation';
import { concatAll, flatMap, map, toArray } from 'rxjs/operators';
import { CategoriesService } from '../../../../../services/categories.service';

@Component({
  selector: 'app-ventilation-edit',
  templateUrl: './ventilation-edit.component.html',
  styleUrls: ['./ventilation-edit.component.css']
})
export class VentilationEditComponent implements OnInit {
  categorySelected: string;
  categories: CategorySelect[];
  @Input() ventilation: Ventilation;
  @Output() ventilationRemoveEvent = new EventEmitter<Ventilation>();

  constructor(private categoriesService: CategoriesService
  ) { }

  ngOnInit() {
    this.categorySelected = this.ventilation.categoryId;
    this.categoriesService.getCategories().pipe(
      flatMap(x => x),
      map(categoryFamily => this.transformCategoryFamilyInCategorySelect(categoryFamily)),
      concatAll(),
      toArray()
    ).subscribe(c => this.categories = c);
  }


  transformCategoryFamilyInCategorySelect(cFamily: CategoryFamily): CategorySelect[] {
    const cs: CategorySelect[] = [];
    cs.push(new CategorySelect(cFamily.name, cFamily.id, cFamily.name));
    cFamily.subCategories
      .map(subCat => new CategorySelect(subCat.name, subCat.id, cFamily.name))
      .forEach(catSelect => cs.push(catSelect));
    return cs;
  }

  removeVentilation(ventilation: Ventilation) {
    this.ventilationRemoveEvent.emit(ventilation);
  }

  categorySelectedChange(categoryId: number) {
    console.log('Selection de la category avec l id ' + categoryId);
    this.ventilation.categoryId = categoryId.toString();
  }
}

class CategorySelect {
  public name: string;
  public id: string;
  public categoryName: string;

  constructor(n: string, id: string, cName: string) {
    this.name = n;
    this.id = id;
    this.categoryName = cName;
  }
}
