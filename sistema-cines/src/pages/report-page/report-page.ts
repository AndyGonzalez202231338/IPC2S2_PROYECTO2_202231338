import { ReportComentariosService } from './../../services/reports/report-comentarios.service';
import { Component } from '@angular/core';
import { Footer } from '../../components/footer/footer';
import { HeaderAdminSistema } from '../../components/header-admin-sistema/header-admin-sistema';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReportSistemaService, ReporteSistema } from '../../services/reports/report-sistema.service';
import { ReportAnunciosService, ReporteAnuncios } from '../../services/reports/report-anuncio.service';
import { ReportSalasPopularesComponent } from '../../components/reports/report-salas-populares-component/report-salas-populares-component';
import { ReportSalasComentsComponent } from '../../components/reports/report-salas-coments-component/report-salas-coments-component';
import { ReportAnuncianteComponent } from '../../components/reports/report-anunciante-component/report-anunciante-component';

@Component({
  selector: 'app-report-page',
  imports: [Footer, HeaderAdminSistema, CommonModule, FormsModule, ReportSalasPopularesComponent, ReportSalasComentsComponent, ReportAnuncianteComponent],
  templateUrl: './report-page.html',
  styleUrl: './report-page.css'
})
export class ReportPage {
  
  // Variables para reporte de ganancias
  fechaInicioGanancias: string = '';
  fechaFinGanancias: string = '';
  
  // Variables para reporte de anuncios
  fechaInicioAnuncios: string = '';
  fechaFinAnuncios: string = '';
  tipoAnuncio: number | null = null;
  periodo: number | null = null;
  
  // Estados
  isLoadingGanancias: boolean = false;
  isLoadingAnuncios: boolean = false;
  errorMessage: string = '';
  
  // CAMBIO: Especificar los tipos válidos para activeTab
  activeTab: 'ganancias' | 'anuncios' | 'salas' | 'comentarios' | 'anunciantes' = 'ganancias';

  // Opciones para filtros
  tiposAnuncio = [
    { id: 1, nombre: 'TEXTO' },
    { id: 2, nombre: 'TEXTO E IMAGEN' },
    { id: 3, nombre: 'TEXTO Y VIDEO' }
  ];

  periodos = [
    { id: 1, nombre: '1_DIA' },
    { id: 2, nombre: '3_DIAS' },
    { id: 3, nombre: '1_SEMANA' },
    { id: 4, nombre: '2_SEMANAS' }
  ];

  constructor(
    private reportGananciasService: ReportSistemaService,
    private reportAnunciosService: ReportAnunciosService,
  ) { 
    this.setDefaultDates();
  }

  private setDefaultDates() {
    const today = new Date();
    const firstDay = new Date(today.getFullYear(), today.getMonth(), 1);
    const lastDay = new Date(today.getFullYear(), today.getMonth() + 1, 0);
    
    this.fechaInicioGanancias = this.formatDate(firstDay);
    this.fechaFinGanancias = this.formatDate(lastDay);
    this.fechaInicioAnuncios = this.formatDate(firstDay);
    this.fechaFinAnuncios = this.formatDate(lastDay);
  }

  private formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = ('0' + (date.getMonth() + 1)).slice(-2);
    const day = ('0' + date.getDate()).slice(-2);
    return `${year}-${month}-${day}`;
  }

  // Métodos para reporte de ganancias
  generarReporteGanancias() {
    if (!this.fechaInicioGanancias || !this.fechaFinGanancias) {
      this.errorMessage = 'Por favor, selecciona ambas fechas para el reporte de ganancias';
      return;
    }

    if (this.fechaInicioGanancias > this.fechaFinGanancias) {
      this.errorMessage = 'La fecha de inicio no puede ser mayor que la fecha de fin';
      return;
    }

    this.isLoadingGanancias = true;
    this.errorMessage = '';

    console.log('Generando Reporte de Ganancias...', {
      fechaInicio: this.fechaInicioGanancias,
      fechaFin: this.fechaFinGanancias
    });

    const reporteData: ReporteSistema = {
      fechaInicio: this.fechaInicioGanancias,
      fechaFin: this.fechaFinGanancias
    };

    this.reportGananciasService.generarReporteSistema(reporteData).subscribe({
      next: (response) => {
        console.log('Reporte de ganancias generado exitosamente', response);
        this.isLoadingGanancias = false;
        this.descargarReporteGananciasPDF();
      },
      error: (error) => {
        console.error('Error generando reporte de ganancias:', error);
        this.errorMessage = 'Error al generar el reporte de ganancias. Intenta nuevamente.';
        this.isLoadingGanancias = false;
      }
    });
  }

  private descargarReporteGananciasPDF() {
    this.reportGananciasService.descargarReporteGananciasPDF(
      this.fechaInicioGanancias, 
      this.fechaFinGanancias
    );
  }

  // Métodos para reporte de anuncios
  generarReporteAnuncios() {
    if (!this.fechaInicioAnuncios || !this.fechaFinAnuncios) {
      this.errorMessage = 'Por favor, selecciona ambas fechas para el reporte de anuncios';
      return;
    }

    if (this.fechaInicioAnuncios > this.fechaFinAnuncios) {
      this.errorMessage = 'La fecha de inicio no puede ser mayor que la fecha de fin';
      return;
    }

    this.isLoadingAnuncios = true;
    this.errorMessage = '';

    console.log('Generando Reporte de Anuncios...', {
      fechaInicio: this.fechaInicioAnuncios,
      fechaFin: this.fechaFinAnuncios,
      tipoAnuncio: this.tipoAnuncio,
      periodo: this.periodo
    });

    const reporteData: ReporteAnuncios = {
      fechaInicio: this.fechaInicioAnuncios,
      fechaFin: this.fechaFinAnuncios,
      tipoAnuncio: this.tipoAnuncio || undefined,
      periodo: this.periodo || undefined
    };

    this.reportAnunciosService.generarReporteAnuncios(reporteData).subscribe({
      next: (response) => {
        console.log('Reporte de anuncios generado exitosamente', response);
        this.isLoadingAnuncios = false;
        this.descargarReporteAnunciosPDF();
      },
      error: (error) => {
        console.error('Error generando reporte de anuncios:', error);
        this.errorMessage = 'Error al generar el reporte de anuncios. Intenta nuevamente.';
        this.isLoadingAnuncios = false;
      }
    });
  }

  private descargarReporteAnunciosPDF() {
    this.reportAnunciosService.descargarReporteAnunciosPDF(
      this.fechaInicioAnuncios, 
      this.fechaFinAnuncios,
      this.tipoAnuncio || undefined,
      this.periodo || undefined
    );
  }

  limpiarFormularioGanancias() {
    this.fechaInicioGanancias = '';
    this.fechaFinGanancias = '';
    this.errorMessage = '';
    this.setDefaultDates();
  }

  limpiarFormularioAnuncios() {
    this.fechaInicioAnuncios = '';
    this.fechaFinAnuncios = '';
    this.tipoAnuncio = null;
    this.periodo = null;
    this.errorMessage = '';
    this.setDefaultDates();
  }

  // CAMBIO: Especificar el tipo de parámetro
  setActiveTab(tab: 'ganancias' | 'anuncios' | 'salas' | 'comentarios' |'anunciantes') {
    this.activeTab = tab;
    this.errorMessage = '';
  }

  getTipoAnuncioNombre(): string {
    const tipo = this.tiposAnuncio.find(t => t.id === this.tipoAnuncio);
    return tipo ? tipo.nombre : '';
  }

  getPeriodoNombre(): string {
    const periodo = this.periodos.find(p => p.id === this.periodo);
    return periodo ? periodo.nombre : '';
  }
}