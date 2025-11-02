import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnuncioCineCardComponent } from './anuncio-cine-card-component';

describe('AnuncioCineCardComponent', () => {
  let component: AnuncioCineCardComponent;
  let fixture: ComponentFixture<AnuncioCineCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AnuncioCineCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AnuncioCineCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
