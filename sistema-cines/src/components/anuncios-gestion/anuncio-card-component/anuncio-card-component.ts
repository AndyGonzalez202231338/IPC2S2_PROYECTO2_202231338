import { Component, EventEmitter, Input, Output } from '@angular/core';
import { AnuncioCompleto } from '../../../models/Anuncio/AnuncioCompleto';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AnunciosService } from '../../../services/anuncios/anuncios.service';
import { HomesService, User } from '../../../services/homes/homes.services';
import { Cine } from '../../../models/Cines/cine';


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

  @Output()
  publicidadAutorizada = new EventEmitter<void>();


  loading = false;
  loadingAutorizacion = false;
  currentUser: User | null = null;
  currentCine: Cine | null = null;
  operationDone: boolean = false;
  
  // Agregar estas propiedades
  tiposAnuncio: any[] = [];
  periodosAnuncio: any[] = [];

  constructor(
    private anunciosService: AnunciosService,
    private homesService: HomesService
  ) {}

  ngOnInit(): void {
    this.currentUser = this.homesService.getCurrentUser();
    
    this.cargarOpciones(); // Cargar tipos y períodos
    this.verificarPublicidad();
  }

  private verificarPublicidad(): void {
    // Si el anuncio no tiene publicidad asignada pero queremos verificar en la BD
    if (!this.selectedAnuncio.publicidad && this.isAdminSistema) {
      this.anunciosService.getPublicidadByAnuncioId(this.selectedAnuncio.idAnuncio).subscribe({
        next: (publicidad) => {
          if (publicidad) {
            console.log('Publicidad encontrada para anuncio:', this.selectedAnuncio.idAnuncio, publicidad);
            this.selectedAnuncio.publicidad = publicidad;
          } else {
            console.log('No se encontró publicidad para anuncio:', this.selectedAnuncio.idAnuncio);
          }
        },
        error: (error) => {
          console.error('Error verificando publicidad:', error);
        }
      });
    }
  }

  // Método para cargar tipos y períodos
  cargarOpciones(): void {
    // Cargar tipos de anuncio
    this.anunciosService.getTiposAnuncio().subscribe({
      next: (tipos) => {
        this.tiposAnuncio = tipos;
        console.log('Tipos de anuncio cargados:', this.tiposAnuncio);
      },
      error: (error) => {
        console.error('Error cargando tipos de anuncio:', error);
      }
    });

    // Cargar períodos de anuncio
    this.anunciosService.getPeriodosAnuncio().subscribe({
      next: (periodos) => {
        this.periodosAnuncio = periodos;
        console.log('Períodos de anuncio cargados:', this.periodosAnuncio);
      },
      error: (error) => {
        console.error('Error cargando períodos de anuncio:', error);
      }
    });
  }

  // Métodos para obtener nombre y duración
  getNombreTipoAnuncio(): string {
    // Si ya viene el objeto completo, usarlo
    if (this.selectedAnuncio.tipoAnuncio) {
      return this.selectedAnuncio.tipoAnuncio.nombre;
    }
    
    // Si no, buscar por ID en el array cargado
    if (this.selectedAnuncio.idTipoAnuncio && this.tiposAnuncio.length > 0) {
      const tipo = this.tiposAnuncio.find(t => t.idTipoAnuncio === this.selectedAnuncio.idTipoAnuncio);
      return tipo?.nombre || `Tipo #${this.selectedAnuncio.idTipoAnuncio}`;
    }
    
    return 'No especificado';
  }

  getDuracionPeriodoAnuncio(): string {
  // Si ya viene el objeto completo, usarlo
  if (this.selectedAnuncio.periodoAnuncio) {
    return this.selectedAnuncio.periodoAnuncio.nombre || 'Período no especificado';
  }
  
  // Si no, buscar por ID en el array cargado
  if (this.selectedAnuncio.idPeriodo && this.periodosAnuncio.length > 0) {
    const periodo = this.periodosAnuncio.find(p => p.idPeriodoAnuncio === this.selectedAnuncio.idPeriodo);
    return periodo?.nombre || `Período #${this.selectedAnuncio.idPeriodo}`;
  }
  
  return 'No especificado';
}

  // Verificar si es administrador de sistema
  get isAdminSistema(): boolean {
    return this.currentUser?.rol?.nombreRol === 'ADMINISTRADOR DE SISTEMA';
  }

  // Mostrar información de publicidad para administradores
  get showPublicidadInfo(): boolean {
    return this.isAdminSistema;
  }

  // Mostrar botón de autorizar si es admin y no tiene publicidad
  get showAutorizarButton(): boolean {
    return this.isAdminSistema && !this.selectedAnuncio.publicidad;
  }

  // Mostrar botón de editar si es admin y ya tiene publicidad
  

  toggleEstado(): void {
    if (this.loading) return;

    const nuevoEstado = this.selectedAnuncio.estado === 'ACTIVO' ? 'INACTIVO' : 'ACTIVO';
    
    if (confirm(`¿Estás seguro de que quieres ${nuevoEstado === 'ACTIVO' ? 'activar' : 'desactivar'} este anuncio?`)) {
      this.loading = true;

      this.anunciosService.cambiarEstadoAnuncio(this.selectedAnuncio.idAnuncio, nuevoEstado).subscribe({
        next: () => {
          this.selectedAnuncio.estado = nuevoEstado;
          this.loading = false;
          this.estadoCambiado.emit();
        },
        error: (error) => {
          console.error('Error al cambiar el estado:', error);
          this.loading = false;
          alert('Error al cambiar el estado del anuncio.');
        }
      });
    }
  }

  // Convertir anuncio a publicidad (crear registro en tabla publicidad)
  autorizarPublicidad(): void {
  const precioBloqueo = prompt('Ingrese el precio por bloqueo para esta publicidad:');
  
  if (!precioBloqueo || isNaN(Number(precioBloqueo)) || Number(precioBloqueo) <= 0) {
    alert('Por favor ingrese un precio válido mayor a 0.');
    return;
  }

  if (confirm(`¿Convertir a publicidad con precio de $ ${precioBloqueo} por bloqueo?`)) {
    this.loadingAutorizacion = true;

    // Solo necesitamos los IDs y el precio
    this.anunciosService.crearPublicidad(
      this.selectedAnuncio.idAnuncio,
      this.selectedAnuncio.idUsuario,
      Number(precioBloqueo)
    ).subscribe({
      next: (publicidad) => {
        // Asignar la publicidad creada al anuncio
        this.selectedAnuncio.publicidad = publicidad;
        this.loadingAutorizacion = false;
        this.operationDone = true;
        this.publicidadAutorizada.emit(); // Emitir después de asignar
        
        // Ocultar el mensaje después de 3 segundos
        setTimeout(() => {
          this.operationDone = false;
        }, 3000);
      },
      error: (error:any) => {
        console.error('Error al crear publicidad:', error);
        this.loadingAutorizacion = false;
        alert('Error al convertir a publicidad.');
      }
    });
  }
}

  // Editar precio de publicidad existente
  editarPublicidad(): void {
    const nuevoPrecio = prompt(
      'Ingrese el nuevo precio por bloqueo:', 
      this.selectedAnuncio.publicidad?.precioBloqueo?.toString()
    );
    
    if (!nuevoPrecio || isNaN(Number(nuevoPrecio)) || Number(nuevoPrecio) <= 0) {
      alert('Por favor ingrese un precio válido mayor a 0.');
      return;
    }

    if (confirm(`¿Actualizar precio a $ ${nuevoPrecio} por bloqueo?`)) {
      this.loadingAutorizacion = true;

      this.anunciosService.actualizarPublicidad(
        this.selectedAnuncio.publicidad!.idPublicidad,
        Number(nuevoPrecio)
      ).subscribe({
        next: (publicidad) => {
          if (this.selectedAnuncio.publicidad) {
            this.selectedAnuncio.publicidad.precioBloqueo = publicidad.precioBloqueo;
          }
          this.loadingAutorizacion = false;
          alert('Precio actualizado exitosamente.');
        },
        error: (error) => {
          console.error('Error al actualizar publicidad:', error);
          this.loadingAutorizacion = false;
          alert('Error al actualizar precio.');
        }
      });
    }
  }

  getPublicidadEstadoBadgeClass(): string {
    if (!this.selectedAnuncio.publicidad) return 'badge bg-secondary';
    
    switch (this.selectedAnuncio.publicidad.estado) {
      case 'ACTIVO': return 'badge bg-success';
      case 'INACTIVO': return 'badge bg-warning';
      case 'VENCIDO': return 'badge bg-danger';
      default: return 'badge bg-secondary';
    }
  }


  getContenidoPreview(): string {
    if (!this.selectedAnuncio.contenidoTexto) return 'Sin contenido';
    return this.selectedAnuncio.contenidoTexto.length > 100 
      ? this.selectedAnuncio.contenidoTexto.substring(0, 100) + '...' 
      : this.selectedAnuncio.contenidoTexto;
  }

  // Métodos de estilo para botones (mantener los que ya tienes)
  getEstadoButtonClass(): string {
    return this.selectedAnuncio.estado === 'ACTIVO' ? 'btn-warning' : 'btn-success';
  }

  getEstadoIconClass(): string {
    return this.selectedAnuncio.estado === 'ACTIVO' ? 'bi-pause-circle' : 'bi-play-circle';
  }

  getEstadoButtonText(): string {
    return this.selectedAnuncio.estado === 'ACTIVO' ? 'Desactivar' : 'Activar';
  }
}