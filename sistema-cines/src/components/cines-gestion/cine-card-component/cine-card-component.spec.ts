import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CineCardComponent } from './cine-card-component';

describe('CineCardComponent', () => {
  let component: CineCardComponent;
  let fixture: ComponentFixture<CineCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CineCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CineCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
