import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HomesService, User } from '../../../services/homes/homes.services';
import { CarteraResponse, CarteraService, CompraRequest, DepositoRequest } from '../../../services/counts/cartera.service';

@Component({
  selector: 'app-cartera-usuario-form',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './cartera-usuario-form.html',
  styleUrl: './cartera-usuario-form.css'
})
export class CarteraUsuarioForm implements OnInit {
  currentUser: User | null = null;
  cartera: CarteraResponse | null = null;
  depositoForm: FormGroup;
  retiroForm: FormGroup;
  isLoading: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';

  constructor(
    private homesService: HomesService,
    private carteraService: CarteraService,
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
    this.currentUser = this.homesService.getCurrentUser();
    
    if (this.currentUser) {
      this.cargarCartera();
    } else {
      this.errorMessage = 'No hay usuario logueado';
    }
  }

  cargarCartera(): void {
    if (!this.currentUser) return;

    this.isLoading = true;
    this.carteraService.getCarteraByUsuario(this.currentUser.idUsuario).subscribe({
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
    if (this.depositoForm.invalid || !this.currentUser) {
      return;
    }

    this.isLoading = true;
    const depositoRequest: DepositoRequest = {
      monto: this.depositoForm.get('monto')?.value
    };

    this.carteraService.realizarDeposito(this.currentUser.idUsuario, depositoRequest).subscribe({
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
    if (this.retiroForm.invalid || !this.currentUser || !this.cartera) {
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

    this.carteraService.realizarCompra(this.currentUser.idUsuario, compraRequest).subscribe({
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

