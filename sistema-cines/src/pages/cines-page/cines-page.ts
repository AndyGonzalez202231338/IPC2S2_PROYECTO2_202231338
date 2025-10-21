import { HeaderAdminSistema } from './../../components/header-admin-sistema/header-admin-sistema';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Footer } from '../../components/footer/footer';
import { Cine } from '../../models/Cines/cine';


@Component({
  selector: 'app-cines-page',
  imports: [RouterLink, Footer, HeaderAdminSistema],
  templateUrl: './cines-page.html',
  styleUrl: './cines-page.css'
})
export class CinesPage {
  protected cines: Cine[] = [];


  

}
