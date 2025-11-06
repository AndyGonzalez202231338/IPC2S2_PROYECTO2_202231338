
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TicketInfo } from '../../../models/ticket/TicketInfo';
import { ComentarioPeliculaModalComponent } from '../../comentarios-gestion/comentario-pelicula-modal-component/comentario-pelicula-modal-component';
import { ComentarioSalaModalComponent } from '../../comentarios-gestion/comentario-sala-modal-component/comentario-sala-modal-component';


@Component({
  selector: 'app-ticket-card-component',
  imports: [CommonModule, ComentarioSalaModalComponent, ComentarioPeliculaModalComponent],
  templateUrl: './ticket-card-component.html',
  styleUrl: './ticket-card-component.css'
})
export class TicketCardComponent {
  @Input({ required: true })
  ticketInfo!: TicketInfo;

  @Output()
  ticketActualizado = new EventEmitter<void>();
  operationDone = false;

  mostrarModalSala = false;
  mostrarModalPelicula = false;

  realizarComentarioSala(): void {
    this.mostrarModalSala = true;
  }

  realizarComentarioPelicula(): void {
    this.mostrarModalPelicula = true;
  }

  onComentarioSalaGuardado(): void {
    console.log('Comentario de sala guardado');
    this.mostrarModalSala = false;
    this.operationDone = true;
    this.ticketActualizado.emit();
    
  }

  onComentarioPeliculaGuardado(): void {
    console.log('Comentario de película guardado');
    this.mostrarModalPelicula = false;
    this.operationDone = true;
    this.ticketActualizado.emit();
  }

  cerrarModalSala(): void {
    this.mostrarModalSala = false;
  }

  cerrarModalPelicula(): void {
    this.mostrarModalPelicula = false;
  }

  getHoraFuncion(): string {
    try {
      const fecha = new Date(this.ticketInfo.funcion.fechaHoraFuncion);
      return fecha.toLocaleTimeString('es-ES', {
        hour: '2-digit',
        minute: '2-digit'
      });
    } catch (error) {
      return 'Hora no disponible';
    }
  }
}