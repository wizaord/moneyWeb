import { UserDetails } from './UserDetails';

export class AccountDetails {
  login: string;
  password: string;
  mail: string;
  users: UserDetails[] = [];

  constructor(login: string, password: string, mail: string) {
    this.login = login;
    this.password = password;
    this.mail = mail;
  }

  addUser(user: UserDetails) {
    this.users.push(user);
  }

  removeUser(user: UserDetails) {
    const index = this.users.indexOf(user, 0);
    if (index > -1) {
      this.users.splice(index, 1);
    }
  }
}
