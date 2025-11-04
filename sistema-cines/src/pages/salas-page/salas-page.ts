import { HomesService } from './../../services/homes/homes.services';
import { Component, OnInit } from '@angular/core';
import { HeaderAdminCine } from '../../components/header-admin-cine/header-admin-cine';
import { Footer } from '../../components/footer/footer';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Sala } from '../../models/Salas/Sala';
import { Cine } from '../../models/Cines/cine';
import { SalasService } from '../../services/salas/salas.service';
import { SalaCardComponent } from "../../components/salas-gestion/sala-card-component/sala-card-component";

@Component({
  selector: 'app-salas-page',
  imports: [CommonModule, RouterLink, Footer, HeaderAdminCine, SalaCardComponent],
  templateUrl: './salas-page.html',
  styleUrl: './salas-page.css'
})
export class SalasPage implements OnInit{
  protected salas: Sala[] = [];
  currentCine: Cine | null = null;
  
  constructor(
      private homesService: HomesService,
      private salasService: SalasService
    ) {}

  ngOnInit(): void {
    this.currentCine = this.homesService.getCineSeleccionado();
    if (this.currentCine) {
      this.loadSalas();
    }
  }

  private loadSalas(): void {
    console.log('Cine del usuario en sesión:', this.currentCine);
    this.salasService.getSalasByCine(this.currentCine?.idCine || 0).subscribe({
      next: (salas) => {
        this.salas = salas;
      },
      error: (error) => {
        console.error('Error al cargar las salas:', error);
      }
    });
  }

  // Método para recargar salas cuando se actualice una
  onSalaActualizada(): void {
    console.log('Sala actualizada, recargando lista...');
    this.loadSalas();
  }
}
