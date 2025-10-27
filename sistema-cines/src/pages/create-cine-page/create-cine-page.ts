import { Component } from '@angular/core';
import { CreateCinesComponent } from '../../components/cines-gestion/create-cines-component/create-cines-component';
import { RouterLink } from '@angular/router';
import { HeaderAdminSistema } from '../../components/header-admin-sistema/header-admin-sistema';
import { Footer } from '../../components/footer/footer';

@Component({
  selector: 'app-create-cine-page',
  imports: [CreateCinesComponent, RouterLink, Footer, HeaderAdminSistema],
  templateUrl: './create-cine-page.html'
})
export class CreateCinePage {

}