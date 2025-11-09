import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportSalasPopularesComponent } from './report-salas-populares-component';

describe('ReportSalasPopularesComponent', () => {
  let component: ReportSalasPopularesComponent;
  let fixture: ComponentFixture<ReportSalasPopularesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportSalasPopularesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReportSalasPopularesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
