import { Component, Input } from '@angular/core';
import { AnuncioCompleto } from '../../../models/Anuncio/AnuncioCompleto';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { PeriodoAnuncio } from '../../../models/Anuncio/PeriodoAnuncio';
import { TipoAnuncio } from '../../../models/Anuncio/TipoAnuncio';
import { AnunciosService } from '../../../services/anuncios/anuncios.service';
import { Anuncio } from '../../../models/Anuncio/anuncio';
import { CommonModule, NgIf } from '@angular/common';
import { HomesService, User } from '../../../services/homes/homes.services';

@Component({
  selector: 'app-create-anuncio-component',
  imports: [NgIf, CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './create-anuncio-component.html',
  styleUrl: './create-anuncio-component.css'
})
export class CreateAnuncioComponent {
  @Input()
  isEditMode: boolean = false;
  @Input()
  isAdminMode: boolean = false;

  @Input()
  anuncioToUpdate!: AnuncioCompleto;

  newAnuncioForm!: FormGroup;
  operationDone: boolean = false;
  tiposAnuncio: TipoAnuncio[] = [];
  periodosAnuncio: PeriodoAnuncio[] = [];
  loading = false;
  submitted = false;
  costoTotal = 0;

  // Solo mantener propiedades para imagen (sin video)
  selectedFile: File | null = null;
  imagePreview: string | ArrayBuffer | null = null;

  userSesion: User | null = null;

  constructor(private fb: FormBuilder, private anunciosService: AnunciosService, private homesService: HomesService) {
    this.newAnuncioForm = this.createForm();
    this.userSesion = this.homesService.getCurrentUser();
  }

  ngOnInit(): void {
    this.cargarOpciones();
    this.setupFormListeners();
  }

  createForm(): FormGroup {
    return this.fb.group({
      idTipoAnuncio: ['', Validators.required],
      idPeriodo: ['', Validators.required],
      titulo: ['', [Validators.required, Validators.minLength(5)]],
      contenido_texto: [''],
      video_url: [''] // URL opcional
    });
  }

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

  setupFormListeners(): void {
    this.newAnuncioForm.get('idTipoAnuncio')?.valueChanges.subscribe(() => {
      this.calcularCostoTotal();
    });

    this.newAnuncioForm.get('idPeriodo')?.valueChanges.subscribe(() => {
      this.calcularCostoTotal();
    });
  }

  calcularCostoTotal(): void {
    const periodo = this.getPeriodoSeleccionado();
    const tipo = this.getTipoSeleccionado();
    this.costoTotal = 500;

    if (tipo) {
      this.costoTotal += tipo.precioAnuncio;
    }

    if (periodo) {
      this.costoTotal += periodo.precioDuracion;
    }
  }

  getTipoSeleccionado(): TipoAnuncio | undefined {
    const tipoId = this.newAnuncioForm.get('idTipoAnuncio')?.value;
    
    if (!tipoId) {
      return undefined;
    }
    
    const tipo = this.tiposAnuncio.find(t => t.idTipoAnuncio === +tipoId);
    return tipo;
  }

  getPeriodoSeleccionado(): PeriodoAnuncio | undefined {
    const periodoId = this.newAnuncioForm.get('idPeriodo')?.value;
    
    if (!periodoId) {
      return undefined;
    }
    
    const periodo = this.periodosAnuncio.find(p => p.idPeriodo === +periodoId);
    return periodo;
  }

  // MÉTODO: Manejar selección de imagen
  onFileSelected(event: any): void {
    const file: File = event.target.files[0];
    
    if (file) {
      // Validar tipo de archivo
      const allowedTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];
      if (!allowedTypes.includes(file.type)) {
        alert('Por favor, selecciona una imagen válida (JPEG, PNG, GIF, WebP)');
        return;
      }
      
      // Validar tamaño (5MB máximo)
      const maxSize = 5 * 1024 * 1024;
      if (file.size > maxSize) {
        alert('La imagen no debe superar los 5MB');
        return;
      }
      
      this.selectedFile = file;
      
      // Crear preview
      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result;
      };
      reader.readAsDataURL(file);
    }
  }

  // MÉTODO: Remover imagen seleccionada
  removeImage(): void {
    this.selectedFile = null;
    this.imagePreview = null;
    const fileInput = document.getElementById('imagenFile') as HTMLInputElement;
    if (fileInput) {
      fileInput.value = '';
    }
  }

  // MÉTODO onSubmit SIMPLIFICADO
  onSubmit(): void {
    this.submitted = true;

    if (this.newAnuncioForm.invalid) {
      console.log('Formulario inválido. Errores:');
      Object.keys(this.newAnuncioForm.controls).forEach(key => {
        const control = this.newAnuncioForm.get(key);
        if (control?.errors) {
          console.log(`Control ${key}:`, control.errors);
        }
      });
      return;
    }

    // Validar que si es tipo con imagen, tenga imagen
    const tipoSeleccionado = this.getTipoSeleccionado();
    
    if (tipoSeleccionado && tipoSeleccionado.nombre.includes('IMAGEN') && !this.selectedFile) {
      alert('Los anuncios con imagen requieren que subas una imagen');
      return;
    }

    this.loading = true;

    // Preparar fechas
    const fechaInicio = new Date().toISOString().replace('Z', '');
    const fechaFin = this.calcularFechaFin();

    // Crear anuncio directamente (solo URL de video, no subir archivos)
    this.crearAnuncioCompleto(fechaInicio, fechaFin);
  }

  // MÉTODO: Crear anuncio completo
  private crearAnuncioCompleto(fechaInicio: string, fechaFin: string): void {
    const formData = new FormData();
    
    // Agregar todos los campos al FormData
    formData.append('id_usuario', this.userSesion ? this.userSesion.idUsuario.toString() : '0');
    formData.append('id_tipo_anuncio', this.newAnuncioForm.get('idTipoAnuncio')?.value);
    formData.append('id_periodo', this.newAnuncioForm.get('idPeriodo')?.value);
    formData.append('titulo', this.newAnuncioForm.get('titulo')?.value);
    formData.append('contenido_texto', this.newAnuncioForm.get('contenido_texto')?.value || '');
    formData.append('video_url', this.newAnuncioForm.get('video_url')?.value || ''); // URL opcional
    formData.append('costo_total', this.costoTotal.toString());
    formData.append('fecha_inicio', fechaInicio);
    formData.append('fecha_fin', fechaFin);
    formData.append('estado', 'ACTIVO');
    
    // Agregar imagen si existe
    if (this.selectedFile) {
      formData.append('imagen', this.selectedFile, this.selectedFile.name);
    }

    console.log('FormData preparado:');
    for (let [key, value] of (formData as any).entries()) {
      console.log(key + ': ', value);
    }

    // Usar el servicio para crear el anuncio
    this.anunciosService.crearAnuncioConImagen(formData).subscribe({
      next: () => {
        this.loading = false;
        this.resetForm();
        this.operationDone = true;
        alert('Anuncio creado exitosamente!');
      },
      error: (error: any) => {
        this.loading = false;
        console.error('Error creando anuncio:', error);
        alert('Error al crear el anuncio. Por favor, intenta nuevamente.');
      }
    });
  }

  calcularFechaFin(): string {
    const periodo = this.getPeriodoSeleccionado();
    
    if (!periodo) {
      const fechaInicio = new Date();
      const fechaFin = new Date();
      fechaFin.setDate(fechaInicio.getDate() + 7); // 7 días por defecto
      return fechaFin.toISOString().replace('Z', '');
    }

    const fechaInicio = new Date();
    const fechaFin = new Date();
    fechaFin.setDate(fechaInicio.getDate() + periodo.diasDuracion);
    
    return fechaFin.toISOString().replace('Z', '');
  }

  resetForm(): void {
    this.submitted = false;
    this.newAnuncioForm.reset();
    this.costoTotal = 0;
    this.selectedFile = null;
    this.imagePreview = null;
    this.operationDone = false;
  }

  // Método para acceder a los controles del formulario
  getControl(controlName: string) {
    return this.newAnuncioForm.get(controlName);
  }

  // Método para verificar si un control tiene errores
  hasError(controlName: string, errorType: string): boolean {
    const control = this.getControl(controlName);
    return control ? control.hasError(errorType) && (control.touched || this.submitted) : false;
  }
}