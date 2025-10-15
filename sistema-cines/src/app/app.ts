import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Header as HeaderComponent } from '../components/header/header';
import { Footer as FooterComponent } from '../components/footer/footer';
import { LoginForm } from "../components/login/login-form/login-form";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('sistema-cines');
}
