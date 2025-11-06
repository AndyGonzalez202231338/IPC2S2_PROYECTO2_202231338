import { Component, EventEmitter, Input, Output } from '@angular/core';
import { HomesService, User } from '../../../services/homes/homes.services';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

import { ComentarioPelicula, ComentarioPeliculaService } from '../../../services/comments/comentario-pelicula.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-comentario-pelicula-modal-component',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './comentario-pelicula-modal-component.html',
  styleUrl: './comentario-pelicula-modal-component.css'
})
export class ComentarioPeliculaModalComponent {
  @Input() idPelicula!: number;
  @Input() tituloPelicula!: string;
  @Output() comentarioGuardado = new EventEmitter<void>();
  @Output() cerrar = new EventEmitter<void>();

  comentarioForm: FormGroup;
  currentUser: User | null = null;
  enviando = false;
  operacionDone = false;
  operacionError = false;

  constructor(
    private fb: FormBuilder,
    private comentarioPeliculaService: ComentarioPeliculaService,
    private homesService: HomesService
  ) {
    this.comentarioForm = this.fb.group({
      comentario: ['', [Validators.required, Validators.minLength(10)]],
      calificacion: [5, [Validators.required, Validators.min(1), Validators.max(5)]]
    });
  }

  ngOnInit(): void {
    this.currentUser = this.homesService.getCurrentUser();
  }

  guardarComentario(): void {
    if (this.comentarioForm.valid && this.currentUser) {
      this.enviando = true;

      const comentario: ComentarioPelicula = {
        idPelicula: this.idPelicula,
        idUsuario: this.currentUser.idUsuario,
        comentario: this.comentarioForm.get('comentario')?.value,
        calificacion: this.comentarioForm.get('calificacion')?.value
      };

      this.comentarioPeliculaService.crearComentario(comentario).subscribe({
        next: (response) => {
          this.operacionDone = true;
          console.log('Comentario de película guardado:', response);
          this.comentarioGuardado.emit();
          this.cerrarModal();
        },
        error: (error) => {
          this.operacionError = true;
          console.error('Error al guardar comentario, ya haz ralizado un comentrio:', error);
          this.enviando = false;
        }
      });
    }
  }

  cerrarModal(): void {
    this.cerrar.emit();
  }

  getStarsArray(): number[] {
    return [1, 2, 3, 4, 5];
  }

  seleccionarCalificacion(calificacion: number): void {
    this.comentarioForm.patchValue({ calificacion });
  }
}