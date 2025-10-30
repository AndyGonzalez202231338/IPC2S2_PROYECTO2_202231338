import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderAdminCine } from './header-admin-cine';

describe('HeaderAdminCine', () => {
  let component: HeaderAdminCine;
  let fixture: ComponentFixture<HeaderAdminCine>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HeaderAdminCine]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HeaderAdminCine);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
