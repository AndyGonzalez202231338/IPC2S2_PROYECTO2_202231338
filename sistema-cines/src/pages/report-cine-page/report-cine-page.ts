import { ReportePeliculasSalas } from './../../services/reports-cine/report-peliculas.service';
import { Component } from '@angular/core';
import { Footer } from '../../components/footer/footer';
import { HeaderAdminCine } from '../../components/header-admin-cine/header-admin-cine';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReportComentsSalasComponent } from '../../components/reports-cine/report-coments-salas-component/report-coments-salas-component';
import { ReportPeliculasComponent } from '../../components/reports-cine/report-peliculas-component/report-peliculas-component';
import { ReporteSalasPopularesComponent } from '../../components/reports-cine/reporte-salas-populares-component/reporte-salas-populares-component';
import { BoletosSalaComponent } from '../../components/reports-cine/boletos-sala-component/boletos-sala-component';
import { BoletosPage } from "../boletos-page/boletos-page";


@Component({
  selector: 'app-report-cine-page',
  imports: [Footer, HeaderAdminCine, CommonModule, FormsModule, ReportComentsSalasComponent, ReportPeliculasComponent, ReporteSalasPopularesComponent, BoletosSalaComponent, BoletosPage],
  templateUrl: './report-cine-page.html',
  styleUrl: './report-cine-page.css'
})
export class ReportCinePage {
  errorMessage: string = '';
  activeTab: 'comentarioSalas' | 'peliculasProyectadas' | 'salasMasGuastadas' | 'boletosVendidos' = 'comentarioSalas';

  // CAMBIO: Especificar el tipo de parámetro
  setActiveTab(tab: 'comentarioSalas' | 'peliculasProyectadas' | 'salasMasGuastadas' | 'boletosVendidos') {
    this.activeTab = tab;
    this.errorMessage = '';
  }


}
