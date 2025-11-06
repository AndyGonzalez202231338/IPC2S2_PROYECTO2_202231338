import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComentarioSalaModalComponent } from './comentario-sala-modal-component';

describe('ComentarioSalaModalComponent', () => {
  let component: ComentarioSalaModalComponent;
  let fixture: ComponentFixture<ComentarioSalaModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ComentarioSalaModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ComentarioSalaModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
