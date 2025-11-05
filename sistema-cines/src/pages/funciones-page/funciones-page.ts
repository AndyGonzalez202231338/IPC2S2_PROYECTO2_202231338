import { Funcion, FunctionsService } from './../../services/function/function.service';
import { Component } from '@angular/core';
import { Footer } from '../../components/footer/footer';
import { HeaderAdminCine } from '../../components/header-admin-cine/header-admin-cine';
import { Cine } from '../../models/Cines/cine';
import { HomesService } from '../../services/homes/homes.services';
import { FuncionesCardComponent } from '../../components/funciones-gestion/funciones-card-component/funciones-card-component';

@Component({
  selector: 'app-funciones-page',
  imports: [Footer, HeaderAdminCine, FuncionesCardComponent],
  templateUrl: './funciones-page.html',
  styleUrl: './funciones-page.css'
})
export class FuncionesPage {
  currentCine: Cine | null = null;
  protected funciones: Funcion[] = []; 
  constructor(
        private homesService: HomesService,
        private functionsService: FunctionsService,
      ) {}

  ngOnInit(): void {
    this.currentCine = this.homesService.getCineSeleccionado();
    if (this.currentCine) {
      this.loadFunciones();
    }
  }

  private loadFunciones(): void {
    if (this.currentCine) {
      this.functionsService.getFunctionsByCine(this.currentCine.idCine).subscribe({
        next: (funciones) => {
          this.funciones = funciones;
        },
        error: (error) => {
          console.error('Error al cargar funciones:', error);
        }
      });
    }
  }
}
