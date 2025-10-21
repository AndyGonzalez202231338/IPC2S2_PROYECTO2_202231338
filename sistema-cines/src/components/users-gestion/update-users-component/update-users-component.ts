
import { CommonModule, NgFor, NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Count } from '../../../models/Counts/count';
import { CountTypeEnum } from '../../../models/Counts/count-type';
import { CountsService } from '../../../services/counts/counts.service';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-update-users-component',
  imports: [NgIf, ReactiveFormsModule, CommonModule, NgFor, RouterLink],
  templateUrl: './update-users-component.html'
})
export class UpdateUsersComponent {
  updateUserForm!: FormGroup;
  userToEdit: Count | null = null;
  isSubmitting = false;

  // Opciones para el select de tipos de cuenta
  countTypeOptions = Object.keys(CountTypeEnum)
    .filter(val => isNaN(Number(val)))
    .map(key => ({
      key: key, 
      value: CountTypeEnum[key as keyof typeof CountTypeEnum]
    }));

    constructor(
    private formBuilder: FormBuilder,
    private countsService: CountsService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadUserToEdit();
    this.initializeForm();
  }

  private loadUserToEdit(): void {
    this.userToEdit = this.countsService.getSelectedUser();
    if (!this.userToEdit) {
      console.error('No hay usuario seleccionado para editar');
      // Puedes redirigir a la lista de usuarios
      this.router.navigate(['/users']);
    }
  }

  private initializeForm(): void {
    if (!this.userToEdit) return;

    this.updateUserForm = this.formBuilder.group({
      id: [this.userToEdit.idUsuario],
      nombreCompleto: [
        this.userToEdit.nombreCompleto, 
        [Validators.required, Validators.maxLength(50)]
      ],
      email: [
        this.userToEdit.email, 
        [Validators.required, Validators.email]
      ],
      password: [
        this.userToEdit.password, 
        [Validators.required, Validators.minLength(3), Validators.maxLength(12)]
      ],
      type: [
        this.userToEdit.rol?.idRol, 
        Validators.required
      ]
    });
  }

  

  onCancel(): void {
    this.router.navigate(['/users']);
  }

  // Getters para fácil acceso en el template
  get name() { return this.updateUserForm.get('name'); }
  get email() { return this.updateUserForm.get('email'); }
  get password() { return this.updateUserForm.get('password'); }
  get type() { return this.updateUserForm.get('type'); }
    
}
