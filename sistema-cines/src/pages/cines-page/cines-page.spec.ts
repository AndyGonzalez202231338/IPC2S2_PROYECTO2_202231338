import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CinesPage } from './cines-page';

describe('CinesPage', () => {
  let component: CinesPage;
  let fixture: ComponentFixture<CinesPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CinesPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CinesPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
