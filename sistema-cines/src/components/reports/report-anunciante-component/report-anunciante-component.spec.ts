import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportAnuncianteComponent } from './report-anunciante-component';

describe('ReportAnuncianteComponent', () => {
  let component: ReportAnuncianteComponent;
  let fixture: ComponentFixture<ReportAnuncianteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportAnuncianteComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReportAnuncianteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
