import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { AnuncioCompleto } from '../../../models/Anuncio/AnuncioCompleto';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AnunciosService, Publicidad, BloqueoPublicidad } from '../../../services/anuncios/anuncios.service';
import { HomesService, User } from '../../../services/homes/homes.services';
import { Cine } from '../../../models/Cines/cine';

@Component({
  selector: 'app-anuncio-cine-card-component',
  imports: [RouterLink, CommonModule],
  templateUrl: './anuncio-cine-card-component.html',
  styleUrl: './anuncio-cine-card-component.css'
})
export class AnuncioCineCardComponent implements OnInit {
  @Input({ required: true })
  selectedAnuncio!: AnuncioCompleto;

  @Output()
  anuncioBloqueado = new EventEmitter<void>();

  loadingBloqueo = false;
  loadingPublicidad = false;
  loadingEstadoBloqueo = false;
  currentUser: User | null = null;
  currentCine: Cine | null = null;
  operationDone: boolean = false;
  
  tiposAnuncio: any[] = [];
  periodosAnuncio: any[] = [];
  publicidadCargada: Publicidad | null = null;
  bloqueoActual: BloqueoPublicidad | null = null;
  bloqueosActivos: BloqueoPublicidad[] = [];

  constructor(
    private anunciosService: AnunciosService,
    private homesService: HomesService
  ) {}

  ngOnInit(): void {
    this.currentUser = this.homesService.getCurrentUser();
    this.currentCine = this.homesService.getCineSeleccionado();
    this.cargarOpciones();
    this.cargarPublicidadSeparadamente();
  }

  cargarOpciones(): void {
    // Cargar tipos de anuncio
    this.anunciosService.getTiposAnuncio().subscribe({
      next: (tipos) => {
        this.tiposAnuncio = tipos;
      },
      error: (error) => {
        console.error('Error cargando tipos de anuncio:', error);
      }
    });

    // Cargar períodos de anuncio
    this.anunciosService.getPeriodosAnuncio().subscribe({
      next: (periodos) => {
        this.periodosAnuncio = periodos;
      },
      error: (error) => {
        console.error('Error cargando períodos de anuncio:', error);
      }
    });
  }

  // Método principal: Cargar publicidad usando getPublicidadByAnuncioId
  cargarPublicidadSeparadamente(): void {
    this.loadingPublicidad = true;
    
    console.log('Cargando publicidad para anuncio ID:', this.selectedAnuncio.idAnuncio);
    
    this.anunciosService.getPublicidadByAnuncioId(this.selectedAnuncio.idAnuncio)
      .subscribe({
        next: (publicidad) => {
          this.loadingPublicidad = false;
          console.log('Publicidad cargada:', publicidad);
          
          this.publicidadCargada = publicidad;
          
          if (publicidad) {
            console.log('Detalles de publicidad:', {
              idPublicidad: publicidad.idPublicidad,
              precioBloqueo: publicidad.precioBloqueo,
              estado: publicidad.estado
            });
            
            // Si tenemos publicidad y un cine, verificar estado de bloqueo
            if (this.currentCine) {
              this.verificarEstadoBloqueo();
            }
          } else {
            console.log('Este anuncio no tiene publicidad asociada');
          }
        },
        error: (error) => {
          this.loadingPublicidad = false;
          console.error('Error cargando publicidad:', error);
          this.publicidadCargada = null;
        }
      });
  }

  // Verificar si el anuncio ya está bloqueado para este cine
  verificarEstadoBloqueo(): void {
    if (!this.currentCine || !this.publicidadCargada) return;

    this.loadingEstadoBloqueo = true;

    this.anunciosService.verificarAnuncioBloqueado(
      this.selectedAnuncio.idAnuncio,
      this.currentCine.idCine
    ).subscribe({
      next: (bloqueo) => {
        this.loadingEstadoBloqueo = false;
        this.bloqueoActual = bloqueo;
        console.log('Estado de bloqueo:', bloqueo);
        
        if (bloqueo) {
          console.log('Anuncio ya bloqueado para este cine');
        }
      },
      error: (error) => {
        this.loadingEstadoBloqueo = false;
        console.error('Error verificando bloqueo:', error);
      }
    });

    // También cargar todos los bloqueos activos del anuncio
    this.anunciosService.getBloqueosActivosPorAnuncio(this.selectedAnuncio.idAnuncio)
      .subscribe({
        next: (bloqueos) => {
          this.bloqueosActivos = bloqueos;
          console.log('Bloqueos activos del anuncio:', bloqueos);
        },
        error: (error) => {
          console.error('Error cargando bloqueos activos:', error);
        }
      });
  }

  // Métodos para obtener nombre y duración
  getNombreTipoAnuncio(): string {
    if (this.selectedAnuncio.tipoAnuncio) {
      return this.selectedAnuncio.tipoAnuncio.nombre;
    }
    
    if (this.selectedAnuncio.idTipoAnuncio && this.tiposAnuncio.length > 0) {
      const tipo = this.tiposAnuncio.find(t => t.idTipoAnuncio === this.selectedAnuncio.idTipoAnuncio);
      return tipo?.nombre || `Tipo #${this.selectedAnuncio.idTipoAnuncio}`;
    }
    
    return 'No especificado';
  }

  getDuracionPeriodoAnuncio(): string {
    if (this.selectedAnuncio.periodoAnuncio) {
      return this.selectedAnuncio.periodoAnuncio.nombre || 'Período no especificado';
    }
    
    if (this.selectedAnuncio.idPeriodo && this.periodosAnuncio.length > 0) {
      const periodo = this.periodosAnuncio.find(p => p.idPeriodoAnuncio === this.selectedAnuncio.idPeriodo);
      return periodo?.nombre || `Período #${this.selectedAnuncio.idPeriodo}`;
    }
    
    return 'No especificado';
  }

  // Bloquear anuncio usando la publicidad cargada
  bloquearAnuncio(): void {
    if (!this.tienePublicidad()) {
      alert('Este anuncio no tiene publicidad configurada. No se puede bloquear.');
      return;
    }

    if (!this.currentCine) {
      alert('No se puede procesar el bloqueo. Verifique la información del cine.');
      return;
    }

    if (this.estaBloqueadoParaMiCine()) {
      alert('Este anuncio ya está bloqueado para su cine.');
      return;
    }

    const precioBloqueo = this.publicidadCargada!.precioBloqueo;
    
    if (confirm(`¿Está seguro de que desea bloquear este anuncio por $${precioBloqueo}?\n\nEsta acción reservará el anuncio exclusivamente para su cine durante el período contratado.`)) {
      this.loadingBloqueo = true;

      this.anunciosService.bloquearAnuncio(
        this.selectedAnuncio.idAnuncio,
        this.currentCine.idCine,
        this.publicidadCargada!.idPublicidad,
        precioBloqueo
      ).subscribe({
        next: (bloqueoCreado) => {
          this.loadingBloqueo = false;
          this.operationDone = true;
          this.bloqueoActual = bloqueoCreado;
          
          console.log('Bloqueo creado exitosamente:', bloqueoCreado);
          
          this.anuncioBloqueado.emit();
          
          // Recargar la publicidad para obtener estado actualizado
          this.cargarPublicidadSeparadamente();
          
          setTimeout(() => {
            this.operationDone = false;
          }, 3000);
          
          alert(`Anuncio bloqueado exitosamente!\n\nFecha de bloqueo: ${this.formatearFecha(bloqueoCreado.fecha_inicio)} a ${this.formatearFecha(bloqueoCreado.fecha_fin)}\nCosto total: $${bloqueoCreado.costo_total}`);
        },
        error: (error: any) => {
          console.error('Error al bloquear anuncio:', error);
          this.loadingBloqueo = false;
          
          let mensajeError = 'Error al bloquear el anuncio. ';
          if (error.error?.message) {
            mensajeError += error.error.message;
          } else {
            mensajeError += 'Verifique su saldo y disponibilidad.';
          }
          
          alert(mensajeError);
        }
      });
    }
  }

  // Verificar si tiene publicidad
  tienePublicidad(): boolean {
    return !!this.publicidadCargada;
  }

  // Verificar si ya está bloqueado para el cine actual
  estaBloqueadoParaMiCine(): boolean {
    return !!this.bloqueoActual;
  }

  // Obtener clase para el badge de estado
  getPublicidadEstadoBadgeClass(): string {
    if (!this.publicidadCargada) return 'badge bg-secondary';
    
    switch (this.publicidadCargada.estado) {
      case 'ACTIVO': return 'badge bg-success';
      case 'INACTIVO': return 'badge bg-warning';
      case 'BLOQUEADO': return 'badge bg-info';
      case 'VENCIDO': return 'badge bg-danger';
      default: return 'badge bg-secondary';
    }
  }

  // Recargar publicidad y estado de bloqueo
  recargarPublicidad(): void {
    this.cargarPublicidadSeparadamente();
  }

  getContenidoPreview(): string {
    if (!this.selectedAnuncio.contenidoTexto) return 'Sin contenido';
    return this.selectedAnuncio.contenidoTexto.length > 100 
      ? this.selectedAnuncio.contenidoTexto.substring(0, 100) + '...' 
      : this.selectedAnuncio.contenidoTexto;
  }

  // Formatear fecha para mostrar
  formatearFecha(fecha: string): string {
    return new Date(fecha).toLocaleDateString('es-ES');
  }

  // Obtener información de cines que han bloqueado este anuncio
  getCinesBloqueadores(): string {
    if (this.bloqueosActivos.length === 0) {
      return 'Ningún cine ha bloqueado este anuncio';
    }
    
    return `${this.bloqueosActivos.length} cine(s) han bloqueado este anuncio`;
  }
}