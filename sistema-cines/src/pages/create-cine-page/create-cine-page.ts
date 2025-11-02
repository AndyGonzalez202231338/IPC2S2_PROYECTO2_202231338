import { Component } from '@angular/core';
import { CreateCinesComponent } from '../../components/cines-gestion/create-cines-component/create-cines-component';
import { RouterLink } from '@angular/router';
import { HeaderAdminSistema } from '../../components/header-admin-sistema/header-admin-sistema';
import { Footer } from '../../components/footer/footer';
import { Header } from "../../components/header/header";

@Component({
  selector: 'app-create-cine-page',
  imports: [CreateCinesComponent, RouterLink, Footer, HeaderAdminSistema, Header],
  templateUrl: './create-cine-page.html'
})
export class CreateCinePage {

}