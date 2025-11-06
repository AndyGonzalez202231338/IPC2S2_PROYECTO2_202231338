import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComentarioPeliculaModalComponent } from './comentario-pelicula-modal-component';

describe('ComentarioPeliculaModalComponent', () => {
  let component: ComentarioPeliculaModalComponent;
  let fixture: ComponentFixture<ComentarioPeliculaModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ComentarioPeliculaModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ComentarioPeliculaModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
