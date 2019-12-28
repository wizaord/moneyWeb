import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { UserDetails } from '../model/UserDetails';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  @Input() user: UserDetails;
  @Output() userRemove = new EventEmitter();

  constructor() { }

  ngOnInit() {
  }

  removeUser() {
    console.log('Remove user');
    this.userRemove.emit();
  }
}
