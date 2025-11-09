import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportPeliculasComponent } from './report-peliculas-component';

describe('ReportPeliculasComponent', () => {
  let component: ReportPeliculasComponent;
  let fixture: ComponentFixture<ReportPeliculasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportPeliculasComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReportPeliculasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
