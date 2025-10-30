import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CarteraUsuarioForm } from './cartera-usuario-form';

describe('CarteraUsuarioForm', () => {
  let component: CarteraUsuarioForm;
  let fixture: ComponentFixture<CarteraUsuarioForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CarteraUsuarioForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CarteraUsuarioForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
