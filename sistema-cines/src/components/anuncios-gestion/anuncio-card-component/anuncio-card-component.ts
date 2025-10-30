import { Component, Input, Output, EventEmitter } from '@angular/core';
import { AnuncioCompleto } from '../../../models/Anuncio/AnuncioCompleto';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AnunciosService } from '../../../services/anuncios/anuncios.service';

@Component({
  selector: 'app-anuncio-card-component',
  imports: [RouterLink, CommonModule],
  templateUrl: './anuncio-card-component.html',
  styleUrl: './anuncio-card-component.css'
})
export class AnuncioCardComponent {
  @Input({ required: true })
  selectedAnuncio!: AnuncioCompleto;

  @Output()
  estadoCambiado = new EventEmitter<void>();

  loading = false;

  constructor(private anunciosService: AnunciosService) {}

  toggleEstado(): void {
    if (this.loading) return;

    const nuevoEstado = this.selectedAnuncio.estado === 'ACTIVO' ? 'INACTIVO' : 'ACTIVO';
    
    if (confirm(`¿Estás seguro de que quieres ${nuevoEstado === 'ACTIVO' ? 'activar' : 'desactivar'} este anuncio?`)) {
      this.loading = true;

      this.anunciosService.cambiarEstadoAnuncio(this.selectedAnuncio.idAnuncio, nuevoEstado).subscribe({
        next: () => {
          // Actualizar el estado localmente
          this.selectedAnuncio.estado = nuevoEstado;
          this.loading = false;
          
          // Emitir evento para notificar al componente padre
          this.estadoCambiado.emit();
          
          console.log(`Estado cambiado a: ${nuevoEstado}`);
        },
        error: (error) => {
          console.error('Error al cambiar el estado:', error);
          this.loading = false;
          alert('Error al cambiar el estado del anuncio. Por favor, intenta nuevamente.');
        }
      });
    }
  }
}