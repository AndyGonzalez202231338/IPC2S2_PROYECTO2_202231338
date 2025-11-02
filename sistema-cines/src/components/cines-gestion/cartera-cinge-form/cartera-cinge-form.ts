import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CarteraCineResponse, CarteraCineService, CompraRequest, DepositoRequest } from '../../../services/cines/cartera-cine.service';
import { CineCartera, HomesService } from '../../../services/homes/homes.services';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-cartera-cinge-form',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './cartera-cinge-form.html',
  styleUrl: './cartera-cinge-form.css'
})
export class CarteraCingeForm {
    cineSeleccionado: CineCartera | null = null;
    cartera: CarteraCineResponse | null = null;
    depositoForm: FormGroup;
    retiroForm: FormGroup;
    isLoading: boolean = false;
    errorMessage: string = '';
    successMessage: string = '';

    constructor(
    private homesService: HomesService,
    private carteraCineService: CarteraCineService,
    private fb: FormBuilder
  ) {
    this.depositoForm = this.fb.group({
      monto: ['', [Validators.required, Validators.min(1), Validators.max(10000)]]
    });

    this.retiroForm = this.fb.group({
      monto: ['', [Validators.required, Validators.min(1)]]
    });
  }

  ngOnInit(): void {
    this.cineSeleccionado = this.homesService.getCineSeleccionado();
    
    if (this.cineSeleccionado) {
      this.cargarCartera();
    } else {
      this.errorMessage = 'No hay cine seleccionado';
    }
  }

  cargarCartera(): void {
    if (!this.cineSeleccionado) return;

    this.isLoading = true;
    this.carteraCineService.getCarteraByCine(this.cineSeleccionado.idCine).subscribe({
      next: (cartera) => {
        this.cartera = cartera;
        this.isLoading = false;
        console.log('Cartera cargada:', cartera);
      },
      error: (error) => {
        console.error('Error cargando cartera:', error);
        this.errorMessage = this.getErrorMessage(error);
        this.isLoading = false;
      }
    });
  }

  realizarDeposito(): void {
      if (this.depositoForm.invalid || !this.cineSeleccionado) {
        return;
      }
  
      this.isLoading = true;
      const depositoRequest: DepositoRequest = {
        monto: this.depositoForm.get('monto')?.value
      };

      this.carteraCineService.realizarDeposito(this.cineSeleccionado.idCine, depositoRequest).subscribe({
        next: (response) => {
          this.successMessage = 'Depósito realizado exitosamente';
          this.cartera = response; // Actualizar cartera con la respuesta
          this.depositoForm.reset();
          this.isLoading = false;
          
          setTimeout(() => {
            this.successMessage = '';
          }, 3000);
        },
        error: (error) => {
          console.error('Error realizando depósito:', error);
          this.errorMessage = this.getErrorMessage(error);
          this.isLoading = false;
        }
      });
    }

    realizarRetiro(): void {
        if (this.retiroForm.invalid || !this.cineSeleccionado || !this.cartera) {
          return;
        }
    
        const montoRetiro = this.retiroForm.get('monto')?.value;
        
        // Validar saldo suficiente
        if (montoRetiro > this.cartera.saldo) {
          this.errorMessage = 'Saldo insuficiente para realizar el retiro';
          return;
        }
    
        this.isLoading = true;
        const compraRequest: CompraRequest = {
          monto: montoRetiro
        };

        this.carteraCineService.realizarCompra(this.cineSeleccionado.idCine, compraRequest).subscribe({
          next: (response) => {
            this.successMessage = 'Retiro realizado exitosamente';
            this.cartera = response; // Actualizar cartera con la respuesta
            this.retiroForm.reset();
            this.isLoading = false;
            
            setTimeout(() => {
              this.successMessage = '';
            }, 3000);
          },
          error: (error) => {
            console.error('Error realizando retiro:', error);
            this.errorMessage = this.getErrorMessage(error);
            this.isLoading = false;
          }
        });
      }

      private getErrorMessage(error: any): string {
    if (error.status === 404) {
      return 'Usuario o cartera no encontrada';
    } else if (error.status === 400) {
      if (error.error && typeof error.error === 'string') {
        return error.error;
      }
      return 'Monto inválido o saldo insuficiente';
    } else if (error.status === 0) {
      return 'Error de conexión con el servidor';
    } else {
      return 'Error en el servidor. Intente nuevamente.';
    }
  }

  // Formatear monto para mostrar
  formatMonto(monto: number): string {
    return `Q ${monto.toFixed(2)}`;
  }

}
