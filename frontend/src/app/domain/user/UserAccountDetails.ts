import { UserDetails } from './UserDetails';

export class UserAccountDetails {
  login: string;
  password: string;
  email: string;
  users: UserDetails[] = [];

  constructor(login: string, password: string, email: string) {
    this.login = login;
    this.password = password;
    this.email = email;
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
