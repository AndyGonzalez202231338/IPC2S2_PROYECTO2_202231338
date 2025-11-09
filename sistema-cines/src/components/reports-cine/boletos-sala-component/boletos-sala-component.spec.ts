import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BoletosSalaComponent } from './boletos-sala-component';

describe('BoletosSalaComponent', () => {
  let component: BoletosSalaComponent;
  let fixture: ComponentFixture<BoletosSalaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BoletosSalaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BoletosSalaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
