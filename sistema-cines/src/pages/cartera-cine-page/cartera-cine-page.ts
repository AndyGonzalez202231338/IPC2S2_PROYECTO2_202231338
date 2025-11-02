import { Component } from '@angular/core';
import { HomesService, User } from '../../services/homes/homes.services';
import { Cine } from '../../models/Cines/cine';
import { Footer } from '../../components/footer/footer';
import { HeaderAdminCine } from '../../components/header-admin-cine/header-admin-cine';
import { CarteraCingeForm } from '../../components/cines-gestion/cartera-cinge-form/cartera-cinge-form';

@Component({
  selector: 'app-cartera-cine-page',
  imports: [Footer, HeaderAdminCine, CarteraCingeForm],
  templateUrl: './cartera-cine-page.html',
  styleUrl: './cartera-cine-page.css'
})
export class CarteraCinePage {
  currentCine: Cine | null = null;

  constructor(private homesService: HomesService) {}
  
    ngOnInit(): void {
      // Obtener el cine actual del servicio
      this.currentCine = this.homesService.getCineSeleccionado();
    }

}
