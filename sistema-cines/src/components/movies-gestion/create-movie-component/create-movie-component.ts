import { CategoriaPeliculaService } from './../../../services/movies/CategoriaPelicula.service';
import { NgFor } from '@angular/common';
import { Component } from '@angular/core';
import { FormGroup, FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MoviesService } from '../../../services/movies/movies.service';
import { CategoriaPelicula } from '../../../models/Movies/CategoriaPelicula';

@Component({
  selector: 'app-create-movie-component',
  imports: [FormsModule, ReactiveFormsModule, NgFor],
  templateUrl: './create-movie-component.html',
  styleUrl: './create-movie-component.css'
})
export class CreateMovieComponent {
  newMovieForm!: FormGroup;
  operationDone: boolean = false;
  categoriasPeliculas: CategoriaPelicula[] = [];
  selectedFile: File | null = null;

  constructor(
    private formBuilder: FormBuilder, 
    private moviesService: MoviesService, 
    private categoriaPeliculaService: CategoriaPeliculaService
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadCategoriasPeliculas();
  }

  private loadCategoriasPeliculas(): void {
    this.categoriaPeliculaService.getAllCategoriaPeliculas().subscribe({
      next: (categorias) => {
        this.categoriasPeliculas = categorias;
        console.log('Categorias de peliculas cargadas de DB:', this.categoriasPeliculas);
      },
      error: (err) => {
        console.error('Error loading categories:', err);
      }
    });
  }

  private initForm(): void {
    this.newMovieForm = this.formBuilder.group({
      titulo: ['', [Validators.required]],
      sinopsis: ['', [Validators.required]],
      duracionMinutos: ['', [Validators.required, Validators.min(1)]],
      director: ['', [Validators.required]],
      reparto: ['', [Validators.required]],
      clasificacion: ['', [Validators.required]],
      fechaEstreno: ['', [Validators.required]],
      posterFile: [null], // Cambiado para manejar archivo
      estado: ['', [Validators.required]],
      categorias: [[], [Validators.required, Validators.minLength(1)]] // Array para múltiples categorías
    });
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      this.newMovieForm.patchValue({ posterFile: file });
    }
  }

  onSubmit(): void {
  if (this.newMovieForm.valid && this.selectedFile) {
    const formData = new FormData();
    
    // Agregar datos básicos de la película - CORREGIDO
    formData.append('titulo', this.newMovieForm.get('titulo')?.value);
    formData.append('sinopsis', this.newMovieForm.get('sinopsis')?.value);
    formData.append('duracionMinutos', this.newMovieForm.get('duracionMinutos')?.value.toString());
    formData.append('director', this.newMovieForm.get('director')?.value);
    formData.append('reparto', this.newMovieForm.get('reparto')?.value);
    formData.append('clasificacion', this.newMovieForm.get('clasificacion')?.value);
    formData.append('fechaEstreno', this.newMovieForm.get('fechaEstreno')?.value);
    formData.append('estado', this.newMovieForm.get('estado')?.value);
    
    // Agregar archivo - CORREGIDO
    formData.append('posterFile', this.selectedFile, this.selectedFile.name);
    
    // Agregar categorías como array - CORREGIDO
    const categorias = this.newMovieForm.get('categorias')?.value;
    categorias.forEach((categoriaId: number, index: number) => {
      formData.append('categorias', categoriaId.toString()); // Sin índice
    });

    // DEBUG: Verificar contenido del FormData
    console.log('=== FormData contenido ===');
    for (let pair of (formData as any).entries()) {
      console.log(pair[0] + ': ' + pair[1]);
    }
    console.log('=== Fin FormData ===');
    
    console.log('Enviando película con categorías:', categorias);
    
    this.moviesService.createMovieWithCategories(formData).subscribe({
      next: () => {
        this.operationDone = true;
        this.newMovieForm.reset();
        this.selectedFile = null;
      },
      error: (err) => {
        console.error('Error creating movie:', err);
      }
    });
  } else {
    Object.keys(this.newMovieForm.controls).forEach(key => {
      this.newMovieForm.get(key)?.markAsTouched();
    });
  }
}
}