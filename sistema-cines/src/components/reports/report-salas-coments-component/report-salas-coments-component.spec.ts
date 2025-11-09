import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportSalasComentsComponent } from './report-salas-coments-component';

describe('ReportSalasComentsComponent', () => {
  let component: ReportSalasComentsComponent;
  let fixture: ComponentFixture<ReportSalasComentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportSalasComentsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReportSalasComentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
