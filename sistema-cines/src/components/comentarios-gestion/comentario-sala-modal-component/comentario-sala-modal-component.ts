import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HomesService, User } from '../../../services/homes/homes.services';
import { ComentarioSala, ComentarioSalaService } from '../../../services/comments/comentario-sala.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-comentario-sala-modal-component',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './comentario-sala-modal-component.html',
  styleUrl: './comentario-sala-modal-component.css'
})
export class ComentarioSalaModalComponent {
  @Input() idSala!: number;
  @Input() nombreSala!: string;
  @Output() comentarioGuardado = new EventEmitter<void>();
  @Output() cerrar = new EventEmitter<void>();
  operationError = false;
  operationDone = false;
  comentarioForm: FormGroup;
  currentUser: User | null = null;
  enviando = false;

  constructor(
    private fb: FormBuilder,
    private comentarioSalaService: ComentarioSalaService,
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

      const comentario: ComentarioSala = {
        idSala: this.idSala,
        idUsuario: this.currentUser.idUsuario,
        comentario: this.comentarioForm.get('comentario')?.value,
        calificacion: this.comentarioForm.get('calificacion')?.value
      };

      this.comentarioSalaService.crearComentario(comentario).subscribe({
        next: (response) => {
          this.operationDone = true;
          console.log('Comentario de sala guardado:', response);
          this.comentarioGuardado.emit();
          this.cerrarModal();
        },
        error: (error) => {
          this.operationError = true;
          console.error('Error al guardar comentario:', error);
          this.enviando = false;
        }
      });
    }
    }

  cerrarModal(): void {
    this.cerrar.emit();
  }

  // Método para generar estrellas de calificación
  getStarsArray(): number[] {
    return [1, 2, 3, 4, 5];
  }

  seleccionarCalificacion(calificacion: number): void {
    this.comentarioForm.patchValue({ calificacion });
  }
}