import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AccountOwner } from '../../../../domain/user/AccountOwner';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  @Input() user: AccountOwner;
  @Output() userRemove = new EventEmitter();

  constructor() { }

  ngOnInit() {
  }

  removeUser() {
    console.log('Remove user');
    this.userRemove.emit();
  }
}
