import { HomesService } from './../../../services/homes/homes.services';
import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { SalasService, Sala } from '../../../services/salas/salas.service';
import { Cine } from '../../../models/Cines/cine';

@Component({
  selector: 'app-sala-create-component',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './sala-create-component.html',
  styleUrl: './sala-create-component.css'
})
export class SalaCreateComponent {
  newSalaForm!: FormGroup;
  operationDone: boolean = false;
  currentCine: Cine | null = null;
  loading: boolean = false;
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder, 
    private salaService: SalasService, 
    private homesService: HomesService
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadCurrentCine();
  }

  private initForm(): void {
    this.newSalaForm = this.fb.group({
      nombreSala: ['', [Validators.required, Validators.maxLength(100)]],
      filas: ['', [Validators.required, Validators.min(1), Validators.max(50)]],
      columnas: ['', [Validators.required, Validators.min(1), Validators.max(50)]],
      permiteComentario: ['true', Validators.required],
      estado: ['ACTIVA', Validators.required]
    });
  }
  
  private loadCurrentCine(): void {
    this.currentCine = this.homesService.getCineSeleccionado();
    
    if (!this.currentCine) {
      this.errorMessage = 'No se ha encontrado información del cine. Por favor, inicie sesión nuevamente.';
      return;
    }
    
    console.log('Cine del usuario en sesión:', this.currentCine);
  }

  submit(): void {
    if (this.newSalaForm.invalid) {
      this.markAllFieldsAsTouched();
      return;
    }

    if (!this.currentCine) {
      this.errorMessage = 'No se ha encontrado información del cine. Por favor, inicie sesión nuevamente.';
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    const formData = this.newSalaForm.value;
    
    const nuevaSala: Sala = {
      idSala: 0, // Se asignará automáticamente en el backend
      idCine: this.currentCine.idCine,
      nombreSala: formData.nombreSala,
      filas: Number(formData.filas),
      columnas: Number(formData.columnas),
      permiteComentario: formData.permiteComentario === 'true' ? 'SI' : 'NO',
      estado: formData.estado
    };

    console.log('Creando sala para el cine:', this.currentCine.nombre);
    console.log('Datos de la sala:', nuevaSala);

    this.salaService.createNewSala(nuevaSala).subscribe({
      next: (salaCreada) => {
        this.loading = false;
        this.operationDone = true;
        this.newSalaForm.reset({
          permiteComentario: 'true',
          estado: 'ACTIVA'
        });
        
        console.log('Sala creada exitosamente:', salaCreada);
      },
      error: (error) => {
        this.loading = false;
        console.error('Error al crear sala:', error);
        this.errorMessage = 'Error al crear la sala. Por favor, intente nuevamente.';
      }
    });
  }

  private markAllFieldsAsTouched(): void {
    Object.keys(this.newSalaForm.controls).forEach(key => {
      this.newSalaForm.get(key)?.markAsTouched();
    });
  }

  // Getters para facilitar el acceso en el template
  get nombreSala() { return this.newSalaForm.get('nombreSala'); }
  get filas() { return this.newSalaForm.get('filas'); }
  get columnas() { return this.newSalaForm.get('columnas'); }
  get permiteComentario() { return this.newSalaForm.get('permiteComentario'); }
  get estado() { return this.newSalaForm.get('estado'); }

  // Calcular capacidad total
  get capacidadTotal(): number {
    const filas = this.filas?.value || 0;
    const columnas = this.columnas?.value || 0;
    return filas * columnas;
  }
}