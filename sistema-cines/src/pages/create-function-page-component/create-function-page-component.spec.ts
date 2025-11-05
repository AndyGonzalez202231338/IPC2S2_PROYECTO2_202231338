import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateFunctionPageComponent } from './create-function-page-component';

describe('CreateFunctionPageComponent', () => {
  let component: CreateFunctionPageComponent;
  let fixture: ComponentFixture<CreateFunctionPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateFunctionPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateFunctionPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
