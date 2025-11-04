import { Component } from '@angular/core';
import { HeaderAdminCine } from "../../components/header-admin-cine/header-admin-cine";
import { Footer } from '../../components/footer/footer';
import { RouterLink } from '@angular/router';
import { SalaCreateComponent } from '../../components/salas-gestion/sala-create-component/sala-create-component';

@Component({
  selector: 'app-create-salas-page',
  imports: [HeaderAdminCine, Footer, RouterLink, SalaCreateComponent],
  templateUrl: './create-salas-page.html',
  styleUrl: './create-salas-page.css'
})
export class CreateSalasPage {

}
