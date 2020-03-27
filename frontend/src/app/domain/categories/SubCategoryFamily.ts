
export class SubCategoryFamily {
  public name: string;
  public id: string;
  public familyGroupName: string;
  public isInternalVirement: boolean;

  constructor(name: string, id: string, familyGroupName: string, isInternalVirement: boolean) {
    this.name = name;
    this.id = id;
    this.familyGroupName = familyGroupName;
    this.isInternalVirement = isInternalVirement;
  }
}
