import { Role } from './../../../models/Counts/count';
import { Component, Input, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators, FormsModule, ReactiveFormsModule } from "@angular/forms";
import { KeyValuePipe, NgFor, NgIf } from "@angular/common";
import { CountsService } from "../../../services/counts/counts.service";
import { Count} from "../../../models/Counts/count";
import { UserToUpdateRequest } from '../../../models/Counts/update-user-request';

@Component({
    selector: 'app-user-form',
    imports: [NgFor, NgIf, FormsModule, ReactiveFormsModule, KeyValuePipe],
    templateUrl: './create-account-form.html',
})
export class createAccountComponent implements OnInit {

    @Input()
    isEditMode: boolean = false;
    @Input()
    isAdminMode: boolean = false;
    @Input()
    userToUpdate!: Count;

    newUserForm!: FormGroup;
    newUser!: Count;
    operationDone: boolean = false;
    availableRoles: Role[] = [];
    isLoading: boolean = false;

    constructor(
        private formBuilder: FormBuilder,
        private countsService: CountsService
    ) {
        this.initializeForm();
    }

    ngOnInit(): void {
        this.loadAvailableRoles();
        
        if (this.isEditMode && this.userToUpdate) {
            this.populateForm();
        } else {
            this.reset();
        }
    }

    private initializeForm(): void {
        this.newUserForm = this.formBuilder.group({
            nombreCompleto: ['', [Validators.required, Validators.maxLength(100)]],
            email: ['', [Validators.required, Validators.email]],
            password: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(12)]],
            rol: [this.isAdminMode ? null : this.getDefaultRole(), this.isAdminMode ? Validators.required : []],
            estado: [this.isAdminMode ? 'ACTIVO' : 'ACTIVO'],
            fechaCreacion: [this.getCurrentDateTimeForInput()]
        });

        // En modo edición, bloquear email
        if (this.isEditMode) {
            this.newUserForm.get('email')?.disable();
        }
        
        // En modo admin, fecha de creación es de solo lectura
        if (this.isAdminMode) {
            this.newUserForm.get('fechaCreacion')?.disable();
        }
    }

    // Método para obtener el rol COMUN por defecto
    getDefaultRole(): Role {
        return {
            idRol: 4,
            nombreRol: 'COMUN',
            descripcion: 'Usuario que compra boletos...'
        };
    }

    private loadAvailableRoles(): void {
        if (this.isAdminMode) {
            this.availableRoles = [
                { idRol: 1, nombreRol: 'ADMINISTRADOR DE SISTEMA', descripcion: 'Usuario encargado de cines, peliculas y reportes...' },
                { idRol: 2, nombreRol: 'ADMINISTRADOR DE CINE', descripcion: 'Usuario encargado de un cine, salas, funciones...' },
                { idRol: 3, nombreRol: 'ANUNCIANTE', descripcion: 'Usuario con capacidad para crear anuncios...' },
                { idRol: 4, nombreRol: 'COMUN', descripcion: 'Usuario que compra boletos...' }
            ];
        }
    }

    private populateForm(): void {
        this.newUserForm.patchValue({
            nombreCompleto: this.userToUpdate.nombreCompleto,
            email: this.userToUpdate.email,
            password: this.userToUpdate.password,
            rol: this.userToUpdate.rol,
            estado: this.userToUpdate.estado,
            fechaCreacion: this.formatDateTimeForInput(this.userToUpdate.fechaCreacion)
        });
    }

    private formatDateTimeForInput(dateTimeString: string): string {
        if (!dateTimeString) {
            return this.getCurrentDateTimeForInput();
        }
        
        try {
            let date: Date;
            
            if (dateTimeString.includes('T')) {
                date = new Date(dateTimeString);
            } else if (dateTimeString.includes(' ')) {
                const isoString = dateTimeString.replace(' ', 'T');
                date = new Date(isoString);
            } else {
                date = new Date(dateTimeString);
            }
            
            if (isNaN(date.getTime())) {
                return this.getCurrentDateTimeForInput();
            }
            
            return date.toISOString().slice(0, 16);
        } catch (error) {
            return this.getCurrentDateTimeForInput();
        }
    }

    getCurrentDateTimeForInput(): string {
        const now = new Date();
        const timezoneOffset = now.getTimezoneOffset() * 60000;
        const localTime = new Date(now.getTime() - timezoneOffset);
        return localTime.toISOString().slice(0, 16);
    }

    submit(): void {
        if (this.newUserForm.valid) {
            console.log('Formulario válido, procediendo con la operación...');
            
            // Asegurar que en modo registro se envíen los valores correctos
            if (!this.isAdminMode) {
                this.ensureDefaultValues();
            }
            
            if (this.isEditMode) {
                this.updateUser();
            } else {
                console.log('Creando nuevo usuario...');
                this.createUser();
            }
        } else {
            this.markAllFieldsAsTouched();
        }
    }

    // Método para asegurar valores por defecto en modo registro
    private ensureDefaultValues(): void {
        const currentValues = this.newUserForm.value;
        
        // Forzar valores por defecto
        this.newUserForm.patchValue({
            rol: this.getDefaultRole(),
            estado: 'ACTIVO',
            fechaCreacion: this.getCurrentDateTimeForInput()
        }, { emitEvent: false });
    }

    reset(): void {
        if (this.isEditMode && this.userToUpdate) {
            this.populateForm();
        } else {
            const defaultValues: any = {
                nombreCompleto: '',
                email: '',
                password: '',
                estado: this.isAdminMode ? 'ACTIVO' : 'ACTIVO',
                fechaCreacion: this.getCurrentDateTimeForInput()
            };
            
            if (this.isAdminMode) {
                defaultValues.rol = null;
            } else {
                defaultValues.rol = this.getDefaultRole();
            }
            
            this.newUserForm.reset(defaultValues);
        }
        this.operationDone = false;
    }

    private createUser(): void {
        // Usar getRawValue() para incluir campos deshabilitados
        const formValues = this.newUserForm.getRawValue();
        
        // Convertir fecha al formato ISO
        const userToCreate = {
            ...formValues,
            fechaCreacion: this.convertToISOFormat(formValues.fechaCreacion)
        };
        
        this.newUser = userToCreate as Count;
        
        this.countsService.createNewCount(this.newUser).subscribe({
            next: () => {
                this.reset();
                this.operationDone = true;
            },
            error: (error: any) => {
                console.log('Error creando usuario:', error);
            }
        });
        console.log('Usuario a crear:', this.newUser);
    }

    private updateUser(): void {
        const formValues = this.newUserForm.getRawValue();
        const fechaCreacionISO = this.convertToISOFormat(this.userToUpdate.fechaCreacion);
        
        const userToUpdateRequest: UserToUpdateRequest = {
            idUsuario: this.userToUpdate.idUsuario,
            rol: formValues.rol,
            email: this.userToUpdate.email,
            password: formValues.password,
            nombreCompleto: formValues.nombreCompleto,
            estado: formValues.estado,
            fechaCreacion: fechaCreacionISO
        };

        console.log('Enviando actualización:', userToUpdateRequest);

        this.countsService.updateUser(this.userToUpdate.email, userToUpdateRequest).subscribe({
            next: (updatedUser) => {
                this.operationDone = true;
                this.userToUpdate = updatedUser;
                setTimeout(() => this.operationDone = false, 3000);
            },
            error: (error: any) => {
                console.error('Error al actualizar usuario:', error);
            }
        });
    }

    private convertToISOFormat(dateTimeString: string): string {
        if (!dateTimeString) {
            return new Date().toISOString();
        }
        
        try {
            let date: Date;
            
            if (dateTimeString.includes('T')) {
                date = new Date(dateTimeString);
            } else if (dateTimeString.includes(' ')) {
                const isoString = dateTimeString.replace(' ', 'T');
                date = new Date(isoString);
            } else {
                date = new Date(dateTimeString);
            }
            
            if (isNaN(date.getTime())) {
                return new Date().toISOString();
            }
            
            return date.toISOString();
        } catch (error) {
            return new Date().toISOString();
        }
    }

    private markAllFieldsAsTouched(): void {
        Object.keys(this.newUserForm.controls).forEach(key => {
            const control = this.newUserForm.get(key);
            control?.markAsTouched();
        });
    }

    compareRoles(role1: Role, role2: Role): boolean {
        return role1 && role2 ? role1.idRol === role2.idRol : role1 === role2;
    }

    getFormTitle(): string {
        return this.isEditMode ? 'Editar Usuario' : 
               this.isAdminMode ? 'Crear Nuevo Usuario' : 'Crear Nueva Cuenta';
    }

    getSubmitButtonText(): string {
        return this.isEditMode ? 'Actualizar Usuario' : 
               this.isAdminMode ? 'Crear Usuario' : 'Crear Cuenta';
    }
}