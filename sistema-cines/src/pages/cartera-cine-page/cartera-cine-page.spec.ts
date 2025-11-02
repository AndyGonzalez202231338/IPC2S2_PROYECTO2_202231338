import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CarteraCinePage } from './cartera-cine-page';

describe('CarteraCinePage', () => {
  let component: CarteraCinePage;
  let fixture: ComponentFixture<CarteraCinePage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CarteraCinePage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CarteraCinePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
