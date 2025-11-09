import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReporteSalasPopularesComponent } from './reporte-salas-populares-component';

describe('ReporteSalasPopularesComponent', () => {
  let component: ReporteSalasPopularesComponent;
  let fixture: ComponentFixture<ReporteSalasPopularesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReporteSalasPopularesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReporteSalasPopularesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
