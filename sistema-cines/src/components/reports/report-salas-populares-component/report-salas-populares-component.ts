import { Component } from '@angular/core';
import { ReporteSalasPopulares, ReportPopularSalasService } from '../../../services/reports/report-popular-salas.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-report-salas-populares-component',
  imports: [CommonModule, FormsModule],
  templateUrl: './report-salas-populares-component.html',
  styleUrl: './report-salas-populares-component.css'
})
export class ReportSalasPopularesComponent {
  
  fechaInicio: string = '';
  fechaFin: string = '';
  
  isLoading: boolean = false;
  errorMessage: string = '';

  constructor(private reportSalasService: ReportPopularSalasService) { 
    this.setDefaultDates();
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

    console.log('Generando Reporte de Salas Populares...', {
      fechaInicio: this.fechaInicio,
      fechaFin: this.fechaFin
    });

    const reporteData: ReporteSalasPopulares = {
      fechaInicio: this.fechaInicio,
      fechaFin: this.fechaFin
    };

    this.reportSalasService.generarReporteSalasPopulares(reporteData).subscribe({
      next: (response) => {
        console.log('Reporte de salas populares generado exitosamente', response);
        this.isLoading = false;
        this.descargarReportePDF();
      },
      error: (error) => {
        console.error('Error generando reporte de salas populares:', error);
        this.errorMessage = 'Error al generar el reporte. Intenta nuevamente.';
        this.isLoading = false;
      }
    });
  }

  private descargarReportePDF() {
    this.reportSalasService.descargarReportSalasPopularPDF(
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
