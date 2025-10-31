import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateMoviesPage } from './create-movies-page';

describe('CreateMoviesPage', () => {
  let component: CreateMoviesPage;
  let fixture: ComponentFixture<CreateMoviesPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateMoviesPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateMoviesPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
