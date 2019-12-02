import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HomepageToolbarComponent } from './homepage-toolbar.component';

describe('HomepageToolbarComponent', () => {
  let component: HomepageToolbarComponent;
  let fixture: ComponentFixture<HomepageToolbarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HomepageToolbarComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HomepageToolbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
