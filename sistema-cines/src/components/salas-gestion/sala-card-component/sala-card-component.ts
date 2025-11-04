import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Sala, SalasService, UpdateSalaRequest } from '../../../services/salas/salas.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-sala-card-component',
  imports: [CommonModule],
  templateUrl: './sala-card-component.html',
  styleUrl: './sala-card-component.css'
})
export class SalaCardComponent {
  @Input({ required: true })
  selectedSala!: Sala;

  @Output()
  salaActualizada = new EventEmitter<void>();

  loadingComentarios: boolean = false;
  loadingEstado: boolean = false;

  constructor(private salasService: SalasService) {}

  // Cambiar estado de comentarios
  toggleComentarios(): void {
    if (this.loadingComentarios) return;

    this.loadingComentarios = true;
    const nuevoValor = this.selectedSala.permiteComentario === 'SI' ? 'NO' : 'SI';

    const updateData: UpdateSalaRequest = {
      permiteComentario: nuevoValor,
      estado: this.selectedSala.estado
    };

    this.actualizarSala(updateData, 'comentarios');
  }

  // Cambiar estado de la sala
  toggleEstado(): void {
    if (this.loadingEstado) return;

    this.loadingEstado = true;
    const nuevoValor = this.selectedSala.estado === 'ACTIVA' ? 'BLOQUEADA' : 'ACTIVA';

    const updateData: UpdateSalaRequest = {
      permiteComentario: this.selectedSala.permiteComentario,
      estado: nuevoValor
    };

    this.actualizarSala(updateData, 'estado');
  }

  private actualizarSala(updateData: UpdateSalaRequest, tipo: 'comentarios' | 'estado'): void {
    this.salasService.updateSala(this.selectedSala.idSala, updateData).subscribe({
      next: (salaActualizada) => {
        // Actualizar la sala localmente
        Object.assign(this.selectedSala, salaActualizada);
        
        // Resetear loading
        if (tipo === 'comentarios') {
          this.loadingComentarios = false;
        } else {
          this.loadingEstado = false;
        }
        
        // Emitir evento para notificar al componente padre
        this.salaActualizada.emit();
        
        console.log('Sala actualizada exitosamente:', salaActualizada);
      },
      error: (error) => {
        // Resetear loading en caso de error
        if (tipo === 'comentarios') {
          this.loadingComentarios = false;
        } else {
          this.loadingEstado = false;
        }
        
        console.error('Error al actualizar sala:', error);
        alert('Error al actualizar la sala. Por favor, intente nuevamente.');
      }
    });
  }

  // ... el resto de los métodos auxiliares se mantienen igual
  getBadgeClassComentarios(): string {
    return this.selectedSala.permiteComentario === 'SI' ? 'badge bg-success' : 'badge bg-danger';
  }

  getBadgeClassEstado(): string {
    return this.selectedSala.estado === 'ACTIVA' ? 'badge bg-success' : 'badge bg-danger';
  }

  getButtonClassComentarios(): string {
    return this.selectedSala.permiteComentario === 'SI' ? 'btn btn-success' : 'btn btn-danger';
  }

  getButtonClassEstado(): string {
    return this.selectedSala.estado === 'ACTIVA' ? 'btn btn-success' : 'btn btn-danger';
  }

  getCapacidadTotal(): number {
    return this.selectedSala.filas * this.selectedSala.columnas;
  }
}