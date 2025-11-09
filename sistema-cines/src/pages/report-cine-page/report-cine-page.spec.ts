import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportCinePage } from './report-cine-page';

describe('ReportCinePage', () => {
  let component: ReportCinePage;
  let fixture: ComponentFixture<ReportCinePage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportCinePage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReportCinePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
