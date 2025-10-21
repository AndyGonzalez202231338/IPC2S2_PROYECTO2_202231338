import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateUserPage } from './update-user-page';

describe('UpdateUserPage', () => {
  let component: UpdateUserPage;
  let fixture: ComponentFixture<UpdateUserPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateUserPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateUserPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
