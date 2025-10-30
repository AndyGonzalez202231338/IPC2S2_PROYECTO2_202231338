import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderAnunciante } from './header-anunciante';

describe('HeaderAnunciante', () => {
  let component: HeaderAnunciante;
  let fixture: ComponentFixture<HeaderAnunciante>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HeaderAnunciante]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HeaderAnunciante);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
