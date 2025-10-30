import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateAnuncioComponent } from './create-anuncio-component';

describe('CreateAnuncioComponent', () => {
  let component: CreateAnuncioComponent;
  let fixture: ComponentFixture<CreateAnuncioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateAnuncioComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateAnuncioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
