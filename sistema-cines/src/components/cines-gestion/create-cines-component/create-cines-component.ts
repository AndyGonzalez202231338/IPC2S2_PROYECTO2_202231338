import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Cine } from '../../../models/Cines/cine';
import { CinesService } from '../../../services/cines/cines.service';
import { CountsService } from '../../../services/counts/counts.service';
import { Count } from '../../../models/Counts/count';

@Component({
  selector: 'app-create-cines-component',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './create-cines-component.html',
  styleUrl: './create-cines-component.css'
})
export class CreateCinesComponent implements OnInit {
  @Input() isEditMode: boolean = false;
  @Input() cineToUpdate!: Cine;

  newCineForm!: FormGroup;
  operationDone: boolean = false;
  
  // Listas
  adminCinesList: Count[] = [];
  selectedAdministradores: number[] = [];
  loading: boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private cinesService: CinesService,
    private countsService: CountsService
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadAdministradores();
    
    if (this.isEditMode && this.cineToUpdate) {
      this.loadExistingAdministradores();
    }
  }

  private initForm(): void {
    this.newCineForm = this.formBuilder.group({
      nombre: ['', [Validators.required, Validators.maxLength(100)]],
      direccion: ['', [Validators.required, Validators.maxLength(255)]]
    });
  }

  private loadAdministradores(): void {
    this.loading = true;
    this.cinesService.getUsuariosAdministradores().subscribe({
      next: (admins) => {
        this.adminCinesList = admins;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading administrators:', err);
        this.loading = false;
        // Fallback: cargar todos los usuarios y filtrar
        this.loadAllUsersAndFilter();
      }
    });
  }

  private loadAllUsersAndFilter(): void {
    this.countsService.getAllUsers().subscribe({
      next: (users) => {
        // Filtrar usuarios con rol ADMIN_CINE
        this.adminCinesList = users.filter(user => 
          user.rol.nombreRol === 'ADMIN_CINE' || user.rol.nombreRol === 'ADMINISTRADOR_CINE' || user.rol.nombreRol === 'ADMINISTRADOR DE CINE'
        );
      },
      error: (err) => {
        console.error('Error loading all users:', err);
      }
    });
  }

  private loadExistingAdministradores(): void {
    if (this.cineToUpdate.idCine) {
      this.cinesService.getAdministradoresByCine(this.cineToUpdate.idCine).subscribe({
        next: (admins) => {
          this.selectedAdministradores = admins.map(admin => admin.id_usuario);
          this.patchFormValues();
        },
        error: (err) => {
          console.error('Error loading existing administrators:', err);
          this.patchFormValues();
        }
      });
    } else {
      this.patchFormValues();
    }
  }

  private patchFormValues(): void {
    this.newCineForm.patchValue({
      nombre: this.cineToUpdate.nombre || '',
      direccion: this.cineToUpdate.direccion || ''
    });
  }

  // Manejar selección múltiple
  onAdministradorSelect(event: any, idUsuario: number): void {
    if (event.target.checked) {
      if (!this.selectedAdministradores.includes(idUsuario)) {
        this.selectedAdministradores.push(idUsuario);
      }
    } else {
      this.selectedAdministradores = this.selectedAdministradores.filter(id => id !== idUsuario);
    }
  }

  isAdministradorSelected(idUsuario: number): boolean {
    return this.selectedAdministradores.includes(idUsuario);
  }

  getAdministradorName(idUsuario: number): string {
    const admin = this.adminCinesList.find(a => a.idUsuario === idUsuario);
    return admin ? admin.nombreCompleto : 'Usuario no encontrado';
  }

  submit(): void {
    if (this.newCineForm.invalid) {
      this.markAllFieldsAsTouched();
      return;
    }

    if (this.selectedAdministradores.length === 0) {
      alert('Debe seleccionar al menos un administrador');
      return;
    }

    const cineData: Cine = {
      ...this.newCineForm.value,
      administradores: this.selectedAdministradores
    };

    if (this.isEditMode) {
      this.updateCine(cineData);
    } else {
      this.createCine(cineData);
    }
  }

  private createCine(cineData: Cine): void {
    this.cinesService.createNewCine(cineData).subscribe({
      next: () => {
        
        this.reset();
        this.operationDone = true;
      },
      error: (err) => {
        console.error('Error creating cine:', err);
        alert('Error al crear el cine. Verifique la consola para más detalles.');
      }
    });
    console.log('El cine fue enviado con esta información:', cineData);
  }

  private updateCine(cineData: Cine): void {
    this.cinesService.updateCine(this.cineToUpdate.idCine!, cineData).subscribe({
      next: (updatedCine) => {
        this.operationDone = true;
        // Actualizar administradores
        this.updateCineAdministradores(updatedCine.idCine!);
      },
      error: (err) => {
        console.error('Error updating cine:', err);
        alert('Error al actualizar el cine');
      }
    });
  }

  private updateCineAdministradores(idCine: number): void {
    // Aquí podrías implementar la lógica para actualizar los administradores
    // Esto dependerá de cómo quieras manejar las actualizaciones en el backend
    console.log('Actualizando administradores para cine:', idCine, this.selectedAdministradores);
  }

  private markAllFieldsAsTouched(): void {
    Object.keys(this.newCineForm.controls).forEach(key => {
      this.newCineForm.get(key)?.markAsTouched();
    });
  }

  // Método para remover administrador
removeAdministrador(idUsuario: number): void {
  this.selectedAdministradores = this.selectedAdministradores.filter(id => id !== idUsuario);
}

  reset(): void {
    this.newCineForm.reset({
      nombre: '',
      direccion: ''
    });
    this.selectedAdministradores = [];
    this.operationDone = false;
  }

  // Para modo edición
  private resetOnEdit(): void {
    this.newCineForm.patchValue({
      nombre: this.cineToUpdate.nombre,
      direccion: this.cineToUpdate.direccion
    });
  }


}