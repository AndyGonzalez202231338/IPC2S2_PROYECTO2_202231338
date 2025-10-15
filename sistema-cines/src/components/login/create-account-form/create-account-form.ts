import { CountTypeEnum } from './../../../models/Counts/count-type';
import { Component, OnInit } from '@angular/core';
import { RouterLink, ActivatedRoute } from '@angular/router'; // ✅ Agrega ActivatedRoute
import { Count } from '../../../models/Counts/count';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CountsService } from '../../../services/counts/counts.service';
import { KeyValuePipe, NgIf } from '@angular/common';

@Component({
  selector: 'app-create-account-form',
  imports: [
    RouterLink, 
    ReactiveFormsModule,
    FormsModule
  ],
  templateUrl: './create-account-form.html',
  styleUrl: './create-account-form.css'
})

export class CreateAccountForm implements OnInit {
  
  isAdminMode: boolean = false; 
  defaultType: CountTypeEnum = CountTypeEnum.COMUN;

  newAccountForm!: FormGroup;
  newCount!: Count;
  countTypeEnums = CountTypeEnum;

  constructor(
    private formBuilder: FormBuilder, 
    private countsService: CountsService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
  try {
    console.log('Inicializando CreateAccountForm...');
    this.isAdminMode = this.route.snapshot.data['isAdminMode'] || false;
    console.log('Modo administrador:', this.isAdminMode);
    this.initializeForm();
    console.log('Formulario inicializado correctamente');
  } catch (error) {
    console.error('Error al inicializar el componente:', error);
  }
}

  get availableCountTypes(): any[] {
    if (this.isAdminMode) {
      return this.countTypeOptions.filter(option => 
        option.key !== 'COMUN'
      );
    } else {
      //En modo público, SOLO muestra COMUN
      return this.countTypeOptions.filter(option => option.key === 'COMUN');
    }
  }

  private initializeForm(): void {
    console.log(Object.keys(CountTypeEnum));
    console.log(Object.values(CountTypeEnum));
    console.log(CountTypeEnum);

    const formConfig: any = {
      name: [null, [Validators.required, Validators.maxLength(50)]],
      email: [null, [Validators.required, Validators.email]],
      password: [null, [Validators.required, Validators.minLength(3), Validators.maxLength(12)]],
    };

    if (this.isAdminMode) {
      formConfig.type = [this.defaultType, Validators.required];
    } else {
      formConfig.type = [CountTypeEnum.COMUN]; //Siempre COMUN en registro público
    }

    this.newAccountForm = this.formBuilder.group(formConfig);
  }

  countTypeOptions = Object.keys(CountTypeEnum).filter(val => isNaN(Number(val))).map(key => ({
    key: key, 
    value: CountTypeEnum[key as keyof typeof CountTypeEnum]
  }));

  submit(): void {
    console.log('Se hizo submit');
    if (this.newAccountForm.valid) {
      this.newCount = this.newAccountForm.value as Count;
      this.countsService.createNewCount(this.newCount).subscribe({
        next: () => {
          this.reset();
          // Redirección después de registro exitoso
        },
        error: (error: any) => {
          console.log(error);
        }
      });
      console.log(this.newCount);
    }
  }

  reset(): void {
    const resetValues: any = {
      email: null,
      name: null,
      password: null,
      type: this.isAdminMode ? this.defaultType : CountTypeEnum.COMUN
    };

    this.newAccountForm.reset(resetValues);
  }

  getSubmitButtonText(): string {
    return this.isAdminMode ? 'Crear Usuario' : 'Crear Cuenta';
  }

  getFormTitle(): string {
    return this.isAdminMode ? 'Crear Nuevo Usuario' : 'Crear Cuenta';
  }
}