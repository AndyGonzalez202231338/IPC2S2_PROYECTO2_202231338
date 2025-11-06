import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Funcion, FunctionsService, Sala } from '../../services/function/function.service';
import { Cine } from '../../models/Cines/cine';
import { CinesService } from '../../services/cines/cines.service';
import { SalasService } from '../../services/salas/salas.service';
import { CommonModule } from '@angular/common';
import { HomesService, User } from '../../services/homes/homes.services';
import { forkJoin, Observable } from 'rxjs';
import { Boleto, BoletoService } from '../../services/ticket/boleto.service';

@Component({
  selector: 'app-ticke-page',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './ticke-page.html',
  styleUrl: './ticke-page.css'
})
export class TickePage implements OnInit{
  funcionForm: FormGroup;
  protected funciones: Funcion[] = [];
  sala: Sala | null = null;
  protected cine: Cine | null = null;
  operationDone: boolean = false;
  idPelicula: number;
  selectedFuncion: Funcion | null = null;
  selectedUsuario: User | null = null;
  cantidadAdultos: number = 0;
  cantidadNinos: number = 0;
  total: number = 0;

  constructor(
    private fb: FormBuilder,
    private functionsService: FunctionsService,
    private cinesService: CinesService,
    private salasService: SalasService,
    private homesService: HomesService,
    private boletoService: BoletoService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.idPelicula = Number(this.route.snapshot.paramMap.get('idPelicula'));
    
    this.funcionForm = this.fb.group({
      funcionId: ['', Validators.required],
      cantidadAdultos: [0, [Validators.required, Validators.min(0)]],
      cantidadNinos: [0, [Validators.required, Validators.min(0)]]
    });
  }

  ngOnInit(): void {
  this.selectedUsuario = this.homesService.getCurrentUser();
  this.loadFunciones();
  
  // Escuchar cambios en los controles para calcular el total
  this.funcionForm.get('cantidadAdultos')?.valueChanges.subscribe(() => this.calcularTotal());
    this.funcionForm.get('cantidadNinos')?.valueChanges.subscribe(() => this.calcularTotal());
  }



protected loadFunciones(): void {
  this.functionsService.getFunctionsByPelicula(this.idPelicula).subscribe({
    next: (funciones) => {
      // Filtrar solo funciones PROGRAMADAS
      this.funciones = funciones.filter(funcion => funcion.estado === 'PROGRAMADA');
      console.log('Funciones cargadas:', this.funciones);
    },
    error: (err) => {
      console.error('Error al cargar las funciones:', err);
    }
  });
}

seleccionarFuncion(funcion: Funcion): void {
  this.selectedFuncion = funcion;
  this.cantidadAdultos = 0;
  this.cantidadNinos = 0;
  this.total = 0;
  
  // Actualizar el formulario
  this.funcionForm.patchValue({
    funcionId: funcion.idFuncion,
    cantidadAdultos: 0,
    cantidadNinos: 0
  });

  // Cargar información de sala y cine
  this.loadSala(funcion.idSala);
}

protected loadSala(idSala: number): void {
  this.salasService.getSalaById(idSala).subscribe({
    next: (sala) => {
      this.sala = sala;
      console.log('Sala cargada:', this.sala);
      this.loadCine(sala.idCine);
    },
    error: (err) => {
      console.error('Error al cargar la sala:', err);
    }
  });
}

protected loadCine(idCine: number): void {
  this.cinesService.getCineById(idCine).subscribe({
    next: (cine) => {
      this.cine = cine;
      console.log('Cine cargado:', this.cine);
    },
    error: (err) => {
      console.error('Error al cargar el cine:', err);
    }
  });
}

// Métodos para incrementar/decrementar cantidades
incrementarAdultos(): void {
  if (this.selectedFuncion && 
      (this.cantidadAdultos + this.cantidadNinos) < this.selectedFuncion.asientosDisponibles) {
    this.cantidadAdultos++;
    this.funcionForm.patchValue({ cantidadAdultos: this.cantidadAdultos });
  }
}

decrementarAdultos(): void {
  if (this.cantidadAdultos > 0) {
    this.cantidadAdultos--;
    this.funcionForm.patchValue({ cantidadAdultos: this.cantidadAdultos });
  }
}

incrementarNinos(): void {
  if (this.selectedFuncion && 
      (this.cantidadAdultos + this.cantidadNinos) < this.selectedFuncion.asientosDisponibles) {
    this.cantidadNinos++;
    this.funcionForm.patchValue({ cantidadNinos: this.cantidadNinos });
  }
}

decrementarNinos(): void {
  if (this.cantidadNinos > 0) {
    this.cantidadNinos--;
    this.funcionForm.patchValue({ cantidadNinos: this.cantidadNinos });
  }
}

calcularTotal(): void {
  if (this.selectedFuncion) {
    this.total = (this.cantidadAdultos * this.selectedFuncion.precioBoletoAdulto) + 
                 (this.cantidadNinos * this.selectedFuncion.precioBoletoNino);
  }
}

// Formatear fecha y hora
formatFechaHora(fechaHora: string): string {
  if (!fechaHora) return 'No disponible';
  
  try {
    const date = new Date(fechaHora);
    return date.toLocaleString('es-ES', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  } catch (error) {
    return 'Fecha inválida';
  }
}

// Comprar boletos - VERSIÓN CON CREACIÓN INDIVIDUAL
comprarBoletos(): void {
  if (this.funcionForm.valid && this.selectedFuncion && this.selectedUsuario) {
    const totalBoletos = this.cantidadAdultos + this.cantidadNinos;
    
    if (totalBoletos === 0) {
      alert('Debe seleccionar al menos un boleto');
      return;
    }

    const boletos: Boleto[] = [];
    const requests: Observable<Boleto>[] = [];

    // Crear requests para boletos de adultos
    for (let i = 0; i < this.cantidadAdultos; i++) {
      const boleto: Boleto = {
        idFuncion: this.selectedFuncion.idFuncion!,
        idUsuario: this.selectedUsuario.idUsuario,
        codigoBoleto: this.boletoService.generarCodigoBoleto(),
        fechaCompra: new Date().toISOString(),
        precioPagado: this.selectedFuncion.precioBoletoAdulto
      };
      boletos.push(boleto);
      requests.push(this.boletoService.createTicket(boleto));
    }

    // Crear requests para boletos de niños
    for (let i = 0; i < this.cantidadNinos; i++) {
      const boleto: Boleto = {
        idFuncion: this.selectedFuncion.idFuncion!,
        idUsuario: this.selectedUsuario.idUsuario,
        codigoBoleto: this.boletoService.generarCodigoBoleto(),
        fechaCompra: new Date().toISOString(),
        precioPagado: this.selectedFuncion.precioBoletoNino
      };
      boletos.push(boleto);
      requests.push(this.boletoService.createTicket(boleto));
    }

    console.log('Creando boletos individualmente:', boletos);

    // Ejecutar todas las requests en paralelo
    forkJoin(requests).subscribe({
      next: (boletosCreados) => {
        this.operationDone = true;
        console.log('Compra exitosa. Boletos creados:', boletosCreados);
        
        // Actualizar asientos disponibles
        if (this.selectedFuncion) {
          this.selectedFuncion.asientosDisponibles -= totalBoletos;
        }

        // Mostrar resumen de compra
        this.mostrarResumenCompra(boletosCreados);
        
        setTimeout(() => {
          this.operationDone = false;
        }, 5000);
      },
      error: (err) => {
        console.error('Error en la compra:', err);
        alert('Error al procesar la compra. Por favor intente nuevamente.');
      }
    });
  }
}

// Mostrar resumen de la compra - VERSIÓN SIMPLIFICADA
private mostrarResumenCompra(boletos: Boleto[]): void {
  const totalBoletos = boletos.length;
  const totalPagado = boletos.reduce((sum, boleto) => sum + boleto.precioPagado, 0);
  
  const mensaje = `
    ¡Compra exitosa!
    
    Resumen:
    - Total de boletos: ${totalBoletos}
    - Total pagado: $${totalPagado}
    
    Códigos de boletos:
    ${boletos.map(b => `• ${b.codigoBoleto} - $${b.precioPagado}`).join('\n')}
    
    ¡Disfrute de la función!
  `;
  
  alert(mensaje);
}

cancelar(): void {
  this.router.navigate(['/movies']);
}
}
