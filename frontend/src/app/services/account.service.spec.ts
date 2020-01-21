import { getTestBed, TestBed } from '@angular/core/testing';

import { AccountService } from './account.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Account } from '../domain/account/Account';

describe('AccountService', () => {
  let injector: TestBed;
  let service: AccountService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AccountService]
    });
    injector = getTestBed();
    service = injector.get(AccountService);
    httpMock = injector.get(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  describe('get Accounts opened', () => {

    it('Closed Accounts are filtered', () => {
      // given
      const accountOpened = new Account('id', 'accountName', 'bankName', new Date(), true);
      const accountClosed = new Account('id2', 'accountName', 'bankName', new Date(), false);

      // when
      service.getOpenedAccounts().subscribe(
        accounts => {
          expect(accounts).toBeDefined();
          expect(accounts.length).toBe(1);
          expect(accounts[0].id).toBe('id');
        },
        error => fail()
      );

      const req = httpMock.expectOne(`${service.API_URL}`);
      expect(req.request.method).toBe('GET');
      req.flush(Array(accountOpened, accountClosed));
    });
  });
});
