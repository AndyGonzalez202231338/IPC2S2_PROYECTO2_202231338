import { HeaderAdminSistema } from './../../components/header-admin-sistema/header-admin-sistema';
import { Component } from '@angular/core';
import { Count } from '../../models/Counts/count';
import { CountsService } from '../../services/counts/counts.service';
import { error } from 'console';
import { RouterLink } from '@angular/router';

import { NgFor } from '@angular/common';
import { Footer } from '../../components/footer/footer';
import { User } from '../../services/homes/homes.services';
import { UserCardComponent } from '../../components/users-gestion/user-card-component/user-card-component';

@Component({
  selector: 'app-users-page',
  imports: [UserCardComponent, RouterLink, Footer, HeaderAdminSistema],
  templateUrl: './users-page.html',
})
export class UsersPage {
  protected users: Count[] = [];

  constructor(private countsService: CountsService) { }

  ngOnInit(): void {
    this.countsService.getAllUsers().subscribe({
      next: (usersFromService: Count[]) => { 
        this.users = usersFromService;
      },
      error: (error: any) => {
        console.error(error);
      }
    });
  }
}
