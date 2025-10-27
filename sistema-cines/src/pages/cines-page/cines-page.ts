import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { HeaderAdminSistema } from './../../components/header-admin-sistema/header-admin-sistema';
import { Footer } from '../../components/footer/footer';
import { CineCardComponent } from "../../components/cines-gestion/cine-card-component/cine-card-component";
import { CommonModule } from '@angular/common';

// Importar servicios y modelos
import { Cine } from '../../models/Cines/cine';
import { Count } from '../../models/Counts/count';
import { CinesService } from '../../services/cines/cines.service';
import { CountsService } from '../../services/counts/counts.service';

@Component({
  selector: 'app-cines-page',
  imports: [CommonModule, RouterLink, Footer, HeaderAdminSistema, CineCardComponent],
  templateUrl: './cines-page.html',
  styleUrl: './cines-page.css'
})
export class CinesPage implements OnInit {
  protected cines: Cine[] = [];
  protected administradoresData: Count[] = [];
  protected loading = true;

  constructor(
    private cinesService: CinesService,
    private countsService: CountsService
  ) {}

  ngOnInit(): void {
    this.loadCines();
  }

  private loadCines(): void {
    this.cinesService.getAllCines().subscribe({
      next: (cinesFromServer: Cine[]) => {
        this.cines = cinesFromServer;
        
        // Muestra toda la información de los cines que llegaron de la base de datos
        console.log('Cines recibidos:', JSON.stringify(this.cines));
        
        // Muestra los administradores de cada cine
        this.cines.forEach(cine => {
          console.log(`Cine: ${cine.nombre}, Administradores: ${JSON.stringify(cine.administradores)}`);
        });

        // Cargar datos completos de administradores
        this.loadAdministradoresData();
      },
      error: (error: any) => {
        console.error('Error cargando cines:', error);
        this.loading = false;
      }
    });
  }

  private loadAdministradoresData(): void {
    this.countsService.getAllUsers().subscribe({
      next: (users: Count[]) => {
        // Extraer todos los IDs únicos de administradores de todos los cines
        const adminIds = this.extractAdminIds(this.cines);
        console.log('IDs de administradores a buscar:', adminIds);
        
        // Filtrar solo los usuarios que son administradores de cines
        this.administradoresData = users.filter(user => 
          adminIds.includes(user.idUsuario)
        );
        
        console.log('Datos de administradores cargados:', this.administradoresData);
        this.loading = false;
      },
      error: (error: any) => {
        console.error('Error cargando administradores:', error);
        this.loading = false;
      }
    });
  }

  private extractAdminIds(cines: Cine[]): number[] {
    const adminIds = new Set<number>();
    
    cines.forEach(cine => {
      if (cine.administradores && cine.administradores.length > 0) {
        cine.administradores.forEach(adminId => adminIds.add(adminId));
      }
    });
    
    return Array.from(adminIds);
  }
}