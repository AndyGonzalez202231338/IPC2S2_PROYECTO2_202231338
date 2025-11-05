import { HomesService } from './../../services/homes/homes.services';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Movie } from '../../models/Movies/Movie';
import { Funcion, FunctionsService, Sala } from '../../services/function/function.service';
import { MoviesService } from '../../services/movies/movies.service';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { SalasService } from '../../services/salas/salas.service';
import { Cine } from '../../models/Cines/cine';


@Component({
  selector: 'app-create-function-page-component',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './create-function-page-component.html',
  styleUrl: './create-function-page-component.css'
})
export class CreateFunctionPageComponent implements OnInit {
  funcionForm: FormGroup;
  salas: Sala[] = [];
  pelicula: Movie | null = null;
  isLoading = false;
  operationDone: boolean = false;
  idPelicula: number;
  currentCine: Cine | null = null;
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private functionsService: FunctionsService,
    private moviesService: MoviesService,
    private salasService: SalasService,
    private homesService: HomesService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.idPelicula = Number(this.route.snapshot.paramMap.get('idPelicula'));
    
    this.funcionForm = this.fb.group({
      idSala: ['', Validators.required],
      fechaHoraFuncion: ['', Validators.required],
      precioBoletoAdulto: ['', [Validators.required, Validators.min(0)]],
      precioBoletoNino: ['', [Validators.min(0)]],
      asientosDisponibles: ['', [Validators.required, Validators.min(1)]]
    });
  }

  ngOnInit(): void {
    this.loadPelicula();
    this.loadCineActual();
    this.loadSalas();
  }

  private loadPelicula(): void {
    this.moviesService.getMovieById(this.idPelicula).subscribe({
      next: (pelicula) => {
        this.pelicula = pelicula;
      },
      error: (err) => {
        console.error('Error al cargar película:', err);
        alert('Error al cargar la película');
        this.router.navigate(['/movies']);
      }
    });
  }

  private loadCineActual(): void {
    this.currentCine = this.homesService.getCineSeleccionado();
    
    if (!this.currentCine) {
      this.errorMessage = 'No se ha encontrado información del cine. Por favor, inicie sesión nuevamente.';
      return;
    }
  } 

  private loadSalas(): void {
    this.salasService.getSalasByCine(this.currentCine?.idCine || 0).subscribe({
      next: (salas) => {
        this.salas = salas;
        console.log('Salas cargadas:', this.salas);
      },
      error: (err) => {
        console.error('Error al cargar salas:', err);
        alert('Error al cargar las salas disponibles');
      }
    });
  }

  onSubmit(): void {
    if (this.funcionForm.valid && this.pelicula) {
      this.isLoading = true;

       const fechaHoraValue = this.funcionForm.value.fechaHoraFuncion;
    
    // Validar que la fecha no sea nula o inválida
    if (!fechaHoraValue) {
      alert('Por favor seleccione una fecha y hora válida');
      this.isLoading = false;
      return;
    }

    // Crear objeto Date a partir del valor del input
    const fechaHoraFuncion = new Date(fechaHoraValue);
    
    // Validar que la fecha sea válida
    if (isNaN(fechaHoraFuncion.getTime())) {
      alert('La fecha y hora seleccionada no es válida');
      this.isLoading = false;
      return;
    }

    // Validar que la fecha no sea en el pasado
    const ahora = new Date();
    if (fechaHoraFuncion <= ahora) {
      alert('La función debe programarse para una fecha y hora futura');
      this.isLoading = false;
      return;
    }

    // Formatear la fecha según el patrón que espera el backend: "yyyy-MM-dd'T'HH:mm"
    const formatDateTimeForBackend = (date: Date): string => {
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');

      return `${year}-${month}-${day}T${hours}:${minutes}`;
    };

    const fechaHoraFormateada = formatDateTimeForBackend(fechaHoraFuncion);


      const funcionData: Funcion = {
        idSala: Number(this.funcionForm.value.idSala),
        idPelicula: this.idPelicula,
        fechaHoraFuncion: fechaHoraFormateada,
        precioBoletoAdulto: this.funcionForm.value.precioBoletoAdulto,
        precioBoletoNino: this.funcionForm.value.precioBoletoNino || 0,
        asientosDisponibles: this.funcionForm.value.asientosDisponibles,
        estado: 'PROGRAMADA'
      };

      this.functionsService.createFunction(funcionData).subscribe({
        next: (response) => {
          this.isLoading = false;
          this.operationDone = true;
          console.log('Función creada:', response);
          this.router.navigate(['/movies']);
        },
        error: (err) => {
          this.isLoading = false;
          console.error('Error al crear función:', err);
          alert('Error al crear la función. Por favor intente nuevamente.');
        }
      });
    } else {
      // Marcar todos los campos como touched para mostrar errores
      Object.keys(this.funcionForm.controls).forEach(key => {
        const control = this.funcionForm.get(key);
        control?.markAsTouched();
      });
    }
  }

  cancelar(): void {
    this.router.navigate(['/movies']);
  }
}
