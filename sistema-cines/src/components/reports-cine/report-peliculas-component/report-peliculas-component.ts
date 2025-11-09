import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Sala, SalasService } from '../../../services/salas/salas.service';
import { HomesService } from '../../../services/homes/homes.services';
import { Cine } from '../../../models/Cines/cine';
import { ReportePeliculasSalas, ReportPeliculasSalasService } from '../../../services/reports-cine/report-peliculas.service';

@Component({
  selector: 'app-report-peliculas-component',
  imports: [CommonModule, FormsModule],
  templateUrl: './report-peliculas-component.html',
  styleUrl: './report-peliculas-component.css'
})
export class ReportPeliculasComponent {
  fechaInicio: string = '';
  fechaFin: string = '';
  idSala: number | null = null; 
  salas: Sala[] = [];
  currentCine: Cine | null = null;
  idCine: number | null = null;

  isLoading: boolean = false;
  errorMessage: string = '';

  constructor(
    private reportPeliculasSalasService: ReportPeliculasSalasService,
    private salasService: SalasService, 
    private homeService: HomesService
  ) { 
    this.setDefaultDates();
    this.obtenerSalas();
  }

  obtenerSalas(){
    this.loadCurrentCine();
    if (!this.currentCine) {
      console.error('Cine actual no disponible.');
      return;
    }
    this.salasService.getSalasByCine(this.currentCine.idCine).subscribe({
      next: (data) => {
          this.salas = data;
          console.log('Lista de salas obtenida:', this.salas);
        },
      error: (error) => {
          console.error('Error al obtener la lista de salas:', error);
        }
    });
  }
  private loadCurrentCine(): void {
    this.currentCine = this.homeService.getCineSeleccionado();
    this.idCine = this.currentCine ? this.currentCine.idCine : null; 
    if (!this.currentCine) {
      this.errorMessage = 'No se ha encontrado información del cine. Por favor, inicie sesión nuevamente.';
      return;
    }
    
    console.log('Cine del usuario en sesión:', this.currentCine);
  }

  private setDefaultDates() {
    const today = new Date();
    const firstDay = new Date(today.getFullYear(), today.getMonth(), 1);
    const lastDay = new Date(today.getFullYear(), today.getMonth() + 1, 0);
      
    this.fechaInicio = this.formatDate(firstDay);
    this.fechaFin = this.formatDate(lastDay);
  }

  private formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = ('0' + (date.getMonth() + 1)).slice(-2);
    const day = ('0' + date.getDate()).slice(-2);
    return `${year}-${month}-${day}`;
  }

    descargarReporteDirecto() {
    if (!this.fechaInicio || !this.fechaFin) {
      this.errorMessage = 'Por favor, selecciona ambas fechas para el reporte';
      return;
    }

    if (this.fechaInicio > this.fechaFin) {
      this.errorMessage = 'La fecha de inicio no puede ser mayor que la fecha de fin';
      return;
    }

    console.log('Descargando reporte directo...', {
      fechaInicio: this.fechaInicio,
      fechaFin: this.fechaFin,
      idSala: this.idSala,
      idCine: this.idCine
    });

    this.reportPeliculasSalasService.descargarReportePeliculasSalasPDF(
      this.fechaInicio, 
      this.fechaFin,
      this.idSala || undefined,
      this.idCine || undefined
    );
  }


  limpiarFormulario() {
    this.fechaInicio = '';
    this.fechaFin = '';
    this.idSala = null; 
    this.errorMessage = '';
    this.setDefaultDates();
  }
}
