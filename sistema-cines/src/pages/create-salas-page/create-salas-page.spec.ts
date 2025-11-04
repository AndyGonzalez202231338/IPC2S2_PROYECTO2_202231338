import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateSalasPage } from './create-salas-page';

describe('CreateSalasPage', () => {
  let component: CreateSalasPage;
  let fixture: ComponentFixture<CreateSalasPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateSalasPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateSalasPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
