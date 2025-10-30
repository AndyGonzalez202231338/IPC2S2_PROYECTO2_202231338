
import { HomesService, User } from './../../../services/homes/homes.services';
import { Footer } from './../../footer/footer';
import { Component, OnInit } from '@angular/core';
import { Header } from '../../header/header';
import { CommonModule } from '@angular/common';
import { HeaderAdminSistema } from '../../header-admin-sistema/header-admin-sistema';
import { HeaderAdminCine } from "../../header-admin-cine/header-admin-cine";
import { HeaderAnunciante } from '../../header-anunciante/header-anunciante';

@Component({
  selector: 'app-home',
  imports: [Header, Footer, CommonModule, HeaderAdminSistema, HeaderAdminCine, HeaderAnunciante],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements OnInit {
  protected normalTitle = 'Home';
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
