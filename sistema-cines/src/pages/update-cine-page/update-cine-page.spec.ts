import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateCinePage } from './update-cine-page';

describe('UpdateCinePage', () => {
  let component: UpdateCinePage;
  let fixture: ComponentFixture<UpdateCinePage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateCinePage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateCinePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
