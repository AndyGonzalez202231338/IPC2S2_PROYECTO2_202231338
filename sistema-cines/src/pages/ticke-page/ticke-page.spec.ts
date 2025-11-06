import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TickePage } from './ticke-page';

describe('TickePage', () => {
  let component: TickePage;
  let fixture: ComponentFixture<TickePage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TickePage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TickePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
