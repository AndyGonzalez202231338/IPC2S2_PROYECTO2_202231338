import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CarteraUsuarioPage } from './cartera-usuario-page';

describe('CarteraUsuarioPage', () => {
  let component: CarteraUsuarioPage;
  let fixture: ComponentFixture<CarteraUsuarioPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CarteraUsuarioPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CarteraUsuarioPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
