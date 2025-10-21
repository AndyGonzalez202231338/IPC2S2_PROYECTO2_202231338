import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateCinesComponent } from './create-cines-component';

describe('CreateCinesComponent', () => {
  let component: CreateCinesComponent;
  let fixture: ComponentFixture<CreateCinesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateCinesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateCinesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
