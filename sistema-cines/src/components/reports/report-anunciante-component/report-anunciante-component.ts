import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReportAnuncianteService, ReporteAnunciante } from '../../../services/reports/report-anunciante.service';
import { Count } from '../../../models/Counts/count';

@Component({
  selector: 'app-report-anunciante-component',
  imports: [CommonModule, FormsModule],
  templateUrl: './report-anunciante-component.html',
  styleUrl: './report-anunciante-component.css'
})
export class ReportAnuncianteComponent {
  fechaInicio: string = '';
  fechaFin: string = '';
  anuncianteSeleccionado: Count | null = null;
  idUsuario: number | null = null;
  anunciantes: Count[] = [];
  
  isLoading: boolean = false;
  errorMessage: string = '';

    constructor(private reporteAnuncianteService: ReportAnuncianteService,) { 
      this.setDefaultDates();
      this.obtenerAnunciantes();
    }

    obtenerAnunciantes(){
      this.reporteAnuncianteService.obtenerListaAnunciantes().subscribe({
        next: (data) => {
          this.anunciantes = data;
          console.log('Lista de anunciantes obtenida:', this.anunciantes);
        },
        error: (error) => {
          console.error('Error al obtener la lista de anunciantes:', error);
        }
      });
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

  generarReporte() {
    if (!this.fechaInicio || !this.fechaFin) {
      this.errorMessage = 'Por favor, selecciona ambas fechas para el reporte';
      return;
    }

    if (this.fechaInicio > this.fechaFin) {
      this.errorMessage = 'La fecha de inicio no puede ser mayor que la fecha de fin';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    console.log('Generando Reporte de Anuncios...', {
      fechaInicio: this.fechaInicio,
      fechaFin: this.fechaFin
    });

    const reporteData: ReporteAnunciante = {
      fechaInicio: this.fechaInicio,
      fechaFin: this.fechaFin,
      idUsuario: this.idUsuario || undefined,
    };

    this.reporteAnuncianteService.generarReporteAnunciante(reporteData).subscribe({
      next: (response) => {
        console.log('Reporte de anunciantes generado exitosamente', response);
        this.isLoading = false;
        this.descargarReportePDF();
      },
      error: (error) => {
        console.error('Error generando reporte de anunciantes:', error);
        this.errorMessage = 'Error al generar el reporte. Intenta nuevamente.';
        this.isLoading = false;
      }
    });
  }

  private descargarReportePDF() {
    this.reporteAnuncianteService.descargarReporteAnunciantePDF(
      this.fechaInicio, 
      this.fechaFin
    );
  }

  limpiarFormulario() {
    this.fechaInicio = '';
    this.fechaFin = '';
    this.errorMessage = '';
    this.setDefaultDates();
  }


}
