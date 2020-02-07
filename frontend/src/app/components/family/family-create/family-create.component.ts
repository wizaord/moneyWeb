import { Component, OnInit } from '@angular/core';
import { AccountOwner } from '../../../domain/user/AccountOwner';
import { FamilyService } from '../../../services/family.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-family-create',
  templateUrl: './family-create.component.html',
  styleUrls: ['./family-create.component.css']
})
export class FamilyCreateComponent implements OnInit {
  user = new AccountOwner('');

  constructor(private familyService: FamilyService,
              private router: Router) { }

  ngOnInit() {
  }

  onCreate() {
    this.familyService.createOwner(this.user)
      .subscribe(() => this.router.navigate(['/family/show']));
  }
}
