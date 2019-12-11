import { Component, OnInit } from '@angular/core';
import { UserServiceService } from '../user-service.service';
import { first } from 'rxjs/operators';
import { User } from '../../_models/user';

@Component({
  selector: 'app-user-home',
  templateUrl: './user-home.component.html',
  styleUrls: ['./user-home.component.css']
})
export class UserHomeComponent implements OnInit {
  private loading = false;
  users: User[];

  constructor(private userSrv: UserServiceService) { }

  ngOnInit() {
    this.loading = true;
    this.userSrv.getAll().pipe(first()).subscribe(users => {
      this.loading = false;
      this.users = users;
    });
  }

}
