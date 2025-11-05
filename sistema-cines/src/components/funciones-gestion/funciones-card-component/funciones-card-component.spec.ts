import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FuncionesCardComponent } from './funciones-card-component';

describe('FuncionesCardComponent', () => {
  let component: FuncionesCardComponent;
  let fixture: ComponentFixture<FuncionesCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FuncionesCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FuncionesCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
