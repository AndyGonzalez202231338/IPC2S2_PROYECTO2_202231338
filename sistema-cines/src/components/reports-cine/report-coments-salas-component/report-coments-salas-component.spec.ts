import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportComentsSalasComponent } from './report-coments-salas-component';

describe('ReportComentsSalasComponent', () => {
  let component: ReportComentsSalasComponent;
  let fixture: ComponentFixture<ReportComentsSalasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportComentsSalasComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReportComentsSalasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
