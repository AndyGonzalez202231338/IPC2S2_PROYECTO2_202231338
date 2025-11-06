// boletos-page.component.ts
import { Component, OnInit } from '@angular/core';
import { Header } from './../../components/header/header';
import { BoletoService } from '../../services/ticket/boleto.service';
import { HomesService, User } from '../../services/homes/homes.services';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Footer } from '../../components/footer/footer';
import { TicketCardComponent } from '../../components/tickets-gestion/ticket-card-component/ticket-card-component';
import { TicketInfoService } from '../../services/ticket/ticket-info.service';
import { TicketInfo } from '../../models/ticket/TicketInfo';

@Component({
  selector: 'app-boletos-page',
  imports: [CommonModule, RouterLink, Footer, Header, TicketCardComponent],
  templateUrl: './boletos-page.html',
  styleUrl: './boletos-page.css'
})
export class BoletosPage implements OnInit {
  protected ticketsInfo: TicketInfo[] = [];
  protected currentUser: User | null = null;

  constructor(
    private homesService: HomesService,
    private ticketInfoService: TicketInfoService
  ) {}

  ngOnInit(): void {
    this.currentUser = this.homesService.getCurrentUser();
    this.loadTicketsInfo();
  }

  protected loadTicketsInfo(): void {
    if (this.currentUser) {
      this.ticketInfoService.getTicketInfoByUserId(this.currentUser.idUsuario).subscribe({
        next: (ticketsInfo) => {
          this.ticketsInfo = ticketsInfo;
          console.log('Tickets info cargados:', this.ticketsInfo);
        },
        error: (error) => {
          console.error('Error al cargar tickets info:', error);
        }
      });
    }
  }

  onTicketActualizado(): void {
    console.log('Ticket actualizado, recargando lista...');
    this.loadTicketsInfo();
  }
}