import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnuncionsPage } from './anuncions-page';

describe('AnuncionsPage', () => {
  let component: AnuncionsPage;
  let fixture: ComponentFixture<AnuncionsPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AnuncionsPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AnuncionsPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
