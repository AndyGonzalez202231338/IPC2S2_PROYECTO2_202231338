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
        // Inicializar el formulario inmediatamente en el constructor
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
            rol: [null, this.isAdminMode ? Validators.required : []],
            estado: ['ACTIVO'],
            fechaCreacion: [this.getCurrentDateTimeForInput()]
        });

        // En modo edición, bloquear email y fechaCreacion
        if (this.isEditMode) {
            this.newUserForm.get('email')?.disable();
            if (this.isAdminMode) {
                this.newUserForm.get('fechaCreacion')?.disable();
            }
        }
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

    // Función robusta para manejar diferentes formatos de fecha
    private formatDateTimeForInput(dateTimeString: string): string {
        if (!dateTimeString) {
            return this.getCurrentDateTimeForInput();
        }
        
        try {
            let date: Date;
            
            // Si la fecha ya está en formato ISO (contiene 'T')
            if (dateTimeString.includes('T')) {
                date = new Date(dateTimeString);
            } 
            // Si está en formato "yyyy-MM-dd HH:mm:ss" (como viene del backend)
            else if (dateTimeString.includes(' ')) {
                // Reemplazar espacio por 'T' para crear formato ISO
                const isoString = dateTimeString.replace(' ', 'T');
                date = new Date(isoString);
            }
            // Si es otro formato, intentar parsear directamente
            else {
                date = new Date(dateTimeString);
            }
            
            // Verificar si la fecha es válida
            if (isNaN(date.getTime())) {
                console.warn('Fecha inválida recibida:', dateTimeString);
                return this.getCurrentDateTimeForInput();
            }
            
            return date.toISOString().slice(0, 16);
        } catch (error) {
            console.error('Error formateando fecha:', error, 'Valor recibido:', dateTimeString);
            return this.getCurrentDateTimeForInput();
        }
    }

    private getCurrentDateTimeForInput(): string {
        const now = new Date();
        // Ajustar por diferencia de zona horaria para input datetime-local
        const timezoneOffset = now.getTimezoneOffset() * 60000;
        const localTime = new Date(now.getTime() - timezoneOffset);
        return localTime.toISOString().slice(0, 16);
    }

    private formatDateTimeForBackend(dateTimeString: string): string {
        if (!dateTimeString) {
            return this.formatDateToBackend(new Date());
        }
        
        try {
            const date = new Date(dateTimeString);
            if (isNaN(date.getTime())) {
                return this.formatDateToBackend(new Date());
            }
            return this.formatDateToBackend(date);
        } catch (error) {
            console.error('Error formateando fecha para backend:', error);
            return this.formatDateToBackend(new Date());
        }
    }

    private formatDateToBackend(date: Date): string {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        const seconds = String(date.getSeconds()).padStart(2, '0');
        
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    }

    submit(): void {
        if (this.newUserForm.valid) {
            console.log('Formulario válido, procediendo con la operación...');
            if (this.isEditMode) {
                this.updateUser();
            } else {
                this.createUser();
            }
        } else {
            this.markAllFieldsAsTouched();
        }
    }

    reset(): void {
        if (this.isEditMode && this.userToUpdate) {
            this.populateForm();
        } else {
            this.newUserForm.reset({
                estado: 'ACTIVO',
                fechaCreacion: this.isAdminMode ? this.getCurrentDateTimeForInput() : ''
            });
        }
        this.operationDone = false;
    }

    private createUser(): void {
    this.newUser = this.newUserForm.value as Count;
    this.countsService.createNewCount(this.newUser).subscribe({
        next: () => {
            this.reset();
            this.operationDone = true;
        },
        error: (error: any) => {
            console.log(error);
        }
    });
    console.log('Este usuario llego del form:', this.newUser);
}

    private updateUser(): void {
    // Obtener valores del formulario
    const formValues = this.newUserForm.getRawValue();
    
    // Convertir la fecha al formato ISO que espera el backend
    const fechaCreacionISO = this.convertToISOFormat(this.userToUpdate.fechaCreacion);
    
    // Crear objeto de actualización con el formato correcto
    const userToUpdateRequest: UserToUpdateRequest = {
        idUsuario: this.userToUpdate.idUsuario,
        rol: formValues.rol,
        email: this.userToUpdate.email, // ← Incluir el email (requerido según tu DTO)
        password: formValues.password,
        nombreCompleto: formValues.nombreCompleto,
        estado: formValues.estado,
        fechaCreacion: fechaCreacionISO // ← Enviar en formato ISO
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

// Nueva función para convertir a formato ISO
private convertToISOFormat(dateTimeString: string): string {
    if (!dateTimeString) {
        return new Date().toISOString();
    }
    
    try {
        let date: Date;
        
        // Si ya está en formato ISO
        if (dateTimeString.includes('T')) {
            date = new Date(dateTimeString);
        } 
        // Si está en formato "yyyy-MM-dd HH:mm:ss"
        else if (dateTimeString.includes(' ')) {
            const isoString = dateTimeString.replace(' ', 'T');
            date = new Date(isoString);
        }
        // Otros formatos
        else {
            date = new Date(dateTimeString);
        }
        
        if (isNaN(date.getTime())) {
            console.warn('Fecha inválida, usando fecha actual');
            return new Date().toISOString();
        }
        
        return date.toISOString();
    } catch (error) {
        console.error('Error convirtiendo fecha a ISO:', error);
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

    // Métodos para el template
    getFormTitle(): string {
        return this.isEditMode ? 'Editar Usuario' : 
               this.isAdminMode ? 'Crear Nuevo Usuario' : 'Crear Nueva Cuenta';
    }

    getSubmitButtonText(): string {
        return this.isEditMode ? 'Actualizar Usuario' : 
               this.isAdminMode ? 'Crear Usuario' : 'Crear Cuenta';
    }
}

