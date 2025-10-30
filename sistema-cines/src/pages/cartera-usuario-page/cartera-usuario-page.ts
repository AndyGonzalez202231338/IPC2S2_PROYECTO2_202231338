import { Component, OnInit } from '@angular/core';
import { HomesService, User } from '../../services/homes/homes.services';
import { Footer } from '../../components/footer/footer';
import { Header } from '../../components/header/header';
import { CarteraUsuarioForm } from '../../components/users-gestion/cartera-usuario-form/cartera-usuario-form';
import { HeaderAnunciante } from '../../components/header-anunciante/header-anunciante';
import { NgSwitch } from '@angular/common';

@Component({
  selector: 'app-cartera-usuario-page',
  imports: [Footer, Header, HeaderAnunciante, CarteraUsuarioForm, NgSwitch],
  templateUrl: './cartera-usuario-page.html',
  styleUrl: './cartera-usuario-page.css'
})
export class CarteraUsuarioPage implements OnInit {
  currentUser: User | null = null;

  constructor(private homesService: HomesService) {}

  ngOnInit(): void {
    // Obtener el usuario actual del servicio
    this.currentUser = this.homesService.getCurrentUser();
    

    if (this.currentUser) {
      console.log('Usuario en home:', this.currentUser);
    }
  }

  
}
