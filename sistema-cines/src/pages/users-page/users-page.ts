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
import { ConfirmationModalComponent } from '../../shared/restapi/components/confirmation-modal-component/confirmation-modal-component';

@Component({
  selector: 'app-users-page',
  imports: [UserCardComponent, RouterLink, Footer, HeaderAdminSistema, ConfirmationModalComponent],
  templateUrl: './users-page.html',
})
export class UsersPage {
  protected users: Count[] = [];
  selectedUser!: Count;
  deleted: boolean = false;

  constructor(private countsService: CountsService) { }

  ngOnInit(): void {
    this.loadData();
  }

  private loadData(): void {
    this.countsService.getAllUsers().subscribe({
      next: (usersFromService: Count[]) => { 
        this.users = usersFromService;
      },
      error: (error: any) => {
        console.error(error);
      }
    });
  }

  onSelectedUser(user: Count): void {
    this.selectedUser = user;
    this.deleted = false;
  }

  deleteUser(): void {
    this.countsService.deleteUser(this.selectedUser.email).subscribe({
      next: () => {
        // deleted
        this.loadData();
        this.deleted = true;
      },
      error: (error: any) => {
        console.log(error);
      }
    });
  }
  
}
