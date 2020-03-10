import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { AuthenticationService } from './authentification/authentication.service';
import { Observable } from 'rxjs';
import { CategoryFamily } from '../domain/categories/CategoryFamily';
import { flatMap, map, toArray } from 'rxjs/operators';
import { SubCategoryFamily } from '../domain/categories/SubCategoryFamily';

@Injectable({
  providedIn: 'root'
})
export class CategoriesService {

  API_URL = `${environment.apiUrl}/categories`;

  constructor(private http: HttpClient,
              private authenticationService: AuthenticationService) {
  }

  getCategories(): Observable<CategoryFamily[]> {
    return this.http.get<CategoryFamily[]>(this.API_URL);
  }

  getCategoriesFlatMapAsSubCategories(): Observable<SubCategoryFamily[]> {
    return this.getCategories().pipe(
      flatMap(x => x),
      map(categoryFamily => this.extractCategoryNameValue(categoryFamily)),
      flatMap(x => x),
      toArray());
  }

  private extractCategoryNameValue(categoryFamily: CategoryFamily): SubCategoryFamily[] {
    const subCategories: SubCategoryFamily[] = [];
    subCategories.push(new SubCategoryFamily(categoryFamily.name, categoryFamily.id));
    categoryFamily.subCategories
      .forEach(subCat => subCategories.push(new SubCategoryFamily(subCat.name, subCat.id)));
    return subCategories;
  }
}
