import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BoletosPage } from './boletos-page';

describe('BoletosPage', () => {
  let component: BoletosPage;
  let fixture: ComponentFixture<BoletosPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BoletosPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BoletosPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
