import { AccountOwner } from './AccountOwner';

export class UserAccountDetails {
  login: string;
  password: string;
  email: string;
  owners: AccountOwner[] = [];

  constructor(login: string, password: string, email: string) {
    this.login = login;
    this.password = password;
    this.email = email;
  }

  addUser(user: AccountOwner) {
    this.owners.push(user);
  }

  removeUser(user: AccountOwner) {
    const index = this.owners.indexOf(user, 0);
    if (index > -1) {
      this.owners.splice(index, 1);
    }
  }
}
