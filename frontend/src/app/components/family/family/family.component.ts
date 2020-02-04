import { Component, OnInit } from '@angular/core';
import { AccountOwner } from '../../../domain/user/AccountOwner';
import { FamilyService } from '../../../services/family.service';

@Component({
  selector: 'app-family',
  templateUrl: './family.component.html',
  styleUrls: ['./family.component.css']
})
export class FamilyComponent implements OnInit {
  members: AccountOwner[];

  constructor(private familyService: FamilyService) { }

  ngOnInit() {
    this.familyService.getOwners().subscribe(
      owners => owners.forEach(it => this.members.push(it))
    );
  }

}
