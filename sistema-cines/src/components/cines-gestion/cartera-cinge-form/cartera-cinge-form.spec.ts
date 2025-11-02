import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CarteraCingeForm } from './cartera-cinge-form';

describe('CarteraCingeForm', () => {
  let component: CarteraCingeForm;
  let fixture: ComponentFixture<CarteraCingeForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CarteraCingeForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CarteraCingeForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
