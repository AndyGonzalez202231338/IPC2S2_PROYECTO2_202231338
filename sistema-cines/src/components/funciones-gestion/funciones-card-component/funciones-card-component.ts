import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Funcion } from '../../../services/function/function.service';
import { Movie } from '../../../models/Movies/Movie';
import { Sala } from '../../../models/Salas/Sala';
import { MoviesService } from '../../../services/movies/movies.service';
import { SalasService } from '../../../services/salas/salas.service';

@Component({
  selector: 'app-funciones-card-component',
  imports: [CommonModule],
  templateUrl: './funciones-card-component.html',
  styleUrl: './funciones-card-component.css'
})
export class FuncionesCardComponent implements OnInit {
  @Input({ required: true })
  selectedFuncion!: Funcion;
  
  selectedPelicula!: Movie;
  selectedSala!: Sala;
  
  // Flags para controlar la carga
  cargandoPelicula: boolean = true;
  cargandoSala: boolean = true;

  constructor(
    private serviceMovie: MoviesService, 
    private serviceSala: SalasService
  ) {}

  ngOnInit(): void {
    console.log('Función recibida:', this.selectedFuncion);
    this.cargarPelicula();
    this.cargarSala();
  }

  // Cargar datos de la película
  cargarPelicula(): void {
    this.cargandoPelicula = true;
    this.serviceMovie.getMovieById(this.selectedFuncion.idPelicula).subscribe({
      next: (pelicula) => {
        this.selectedPelicula = pelicula;
        this.cargandoPelicula = false;
        console.log('Película cargada:', this.selectedPelicula);
      },
      error: (error) => {
        console.error('Error al cargar la película:', error);
        this.cargandoPelicula = false;
      }
    });
  }

  // Cargar datos de la sala
  cargarSala(): void {
    this.cargandoSala = true;
    this.serviceSala.getSalaById(this.selectedFuncion.idSala).subscribe({
      next: (sala) => {
        this.selectedSala = sala;
        this.cargandoSala = false;
        console.log('Sala cargada:', this.selectedSala);
      },
      error: (error) => {
        console.error('Error al cargar la sala:', error);
        this.cargandoSala = false;
      }
    });
  }

  // Formatear fecha y hora
  formatFechaHora(fechaHora: string): string {
    if (!fechaHora) return 'No disponible';
    
    try {
      const date = new Date(fechaHora);
      return date.toLocaleString('es-ES', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
    } catch (error) {
      return 'Fecha inválida';
    }
  }

  // Cambiar estado de la función
  cambiarEstado(): void {
    const nuevoEstado = this.selectedFuncion.estado === 'PROGRAMADA' ? 'CANCELADA' : 'PROGRAMADA';
    
    console.log(`Cambiando estado de función ${this.selectedFuncion.idFuncion} a: ${nuevoEstado}`);
    
    this.selectedFuncion.estado = nuevoEstado;
  }

  // Editar función
  editarFuncion(): void {
    console.log('Editando función:', this.selectedFuncion.idFuncion);
    // Navegar a página de edición o abrir modal
  }

  // Verificar si la función está en el pasado
  esFuncionPasada(): boolean {
    if (!this.selectedFuncion.fechaHoraFuncion) return false;
    
    try {
      const fechaFuncion = new Date(this.selectedFuncion.fechaHoraFuncion);
      const ahora = new Date();
      return fechaFuncion < ahora;
    } catch (error) {
      return false;
    }
  }

  // Verificar si todos los datos están cargados
  datosCompletos(): boolean {
    return !this.cargandoPelicula && !this.cargandoSala && !!this.selectedPelicula && !!this.selectedSala;
  }
}