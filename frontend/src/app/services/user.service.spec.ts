import { getTestBed, TestBed } from '@angular/core/testing';

import { UserService } from './user.service';
import { UserAccountDetails } from '../domain/user/UserAccountDetails';
import { UserDetails } from '../domain/user/UserDetails';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

describe('UserService', () => {
  let injector: TestBed;
  let service: UserService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [UserService]
    });
    injector = getTestBed();
    service = injector.get(UserService);
    httpMock = injector.get(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('create user', () => {

    it('Create user without login is not valid', () => {
      // given
      const userAccountDetails = new UserAccountDetails('', '', '');

      // when
      service.createUser(userAccountDetails).subscribe(
        value => fail()
        ,
        error => expect(error).toBeDefined()
      );

    });

    it('Create user without password is not valid', () => {
      // given
      const userAccountDetails = new UserAccountDetails('login', '', '');

      // when
      service.createUser(userAccountDetails).subscribe(
        value => fail()
        ,
        error => expect(error).toBeDefined()
      );
    });

    it('Create user without email is not valid', () => {
      // given
      const userAccountDetails = new UserAccountDetails('login', 'password', '');

      // when
      service.createUser(userAccountDetails).subscribe(
        value => fail()
        ,
        error => expect(error).toBeDefined()
      );
    });

    it('Create user without username is not valid', () => {
      // given
      const userAccountDetails = new UserAccountDetails('login', 'password', 'email');

      // when
      service.createUser(userAccountDetails).subscribe(
        value => fail()
        ,
        error => expect(error).toBeDefined()
      );
    });

    it('Create user with a empty username is not valid', () => {
      // given
      const userAccountDetails = new UserAccountDetails('login', 'password', 'email');
      userAccountDetails.addUser(new UserDetails(''));
      userAccountDetails.addUser(new UserDetails('hello'));

      // when
      service.createUser(userAccountDetails).subscribe(
        value => fail()
        ,
        error => expect(error).toBeDefined()
      );
    });

    it('When create user, return user', () => {
      const userAccountDetails = new UserAccountDetails('login', 'password', 'email');
      userAccountDetails.addUser(new UserDetails('hello'));

      service.createUser(userAccountDetails).subscribe(
        user => {
          expect(user).toBeDefined();
          expect(user.login).toEqual('login');
        },
        error => fail()
      );

      const req = httpMock.expectOne(`${service.API_URL}/create`);
      expect(req.request.method).toBe('POST');
      req.flush(userAccountDetails);
    });
  });


});
