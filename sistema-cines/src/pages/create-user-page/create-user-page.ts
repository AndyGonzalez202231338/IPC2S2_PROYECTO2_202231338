import { Component } from "@angular/core";
import { RouterLink } from "@angular/router";
import { createAccountComponent } from "../../components/login/create-account-form/create-account-form";

@Component({
  selector: 'app-create-user-page',
  imports: [createAccountComponent, RouterLink],
  templateUrl: './create-user-page.html',
})
export class CreateUserPageComponent {
  // Este componente simplemente envuelve el formulario en modo creación admin
}