
export class SubCategoryFamily {
  public name: string;
  public id: string;
  public familyGroupName?: string;

  constructor(name: string, id: string, familyGroupName?: string) {
    this.name = name;
    this.id = id;
    this.familyGroupName = familyGroupName;
  }
}
