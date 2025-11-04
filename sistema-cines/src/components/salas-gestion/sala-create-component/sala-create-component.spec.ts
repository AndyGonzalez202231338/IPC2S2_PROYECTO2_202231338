import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SalaCreateComponent } from './sala-create-component';

describe('SalaCreateComponent', () => {
  let component: SalaCreateComponent;
  let fixture: ComponentFixture<SalaCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SalaCreateComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SalaCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
