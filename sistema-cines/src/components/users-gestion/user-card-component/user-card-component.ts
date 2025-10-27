import { User } from './../../../services/homes/homes.services';
import { Component, EventEmitter, Input, Output } from "@angular/core";
import { Count } from "../../../models/Counts/count";
import { RouterLink } from "@angular/router";
import { RoleGuardService } from '../../../services/security/role-guard.service';

@Component({
  selector: 'app-user-card',
  imports: [RouterLink],
  templateUrl: './user-card-component.html',
})
export class UserCardComponent {
  @Input({ required: true })
  user!: Count;

  @Output()
  userSelected = new EventEmitter<Count>();

  isAdmin: boolean;

  constructor(private roleGuardService: RoleGuardService) {
    this.isAdmin = this.roleGuardService.userRoleInAllowedRoles(['ADMINISTRADOR DE SISTEMA']);
  }
  deleteAction(): void {
    this.userSelected.emit(this.user);
  }
}