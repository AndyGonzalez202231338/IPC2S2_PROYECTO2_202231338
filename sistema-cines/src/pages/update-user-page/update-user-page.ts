import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, RouterLink } from "@angular/router";
import { createAccountComponent } from "../../components/login/create-account-form/create-account-form";
import { CountsService } from "../../services/counts/counts.service";
import { Count } from "../../models/Counts/count";
import { Footer } from "../../components/footer/footer";
import { HeaderAdminSistema } from "../../components/header-admin-sistema/header-admin-sistema";


@Component({
  selector: 'app-update-user-page',
  imports: [createAccountComponent, RouterLink, Footer, HeaderAdminSistema],
  templateUrl: './update-user-page.html',
})
export class UpdateUserPageComponent implements OnInit {
  userEmail!: string;
  userToUpdate!: Count;
  exists: boolean = false;

  constructor(
    private route: ActivatedRoute, 
    private countsService: CountsService
  ) {}

  ngOnInit(): void {
    this.userEmail = this.route.snapshot.params['email'];
    this.countsService.getCountByEmail(this.userEmail).subscribe({
      next: (userToUpdate) => {
        this.userToUpdate = userToUpdate;
        this.exists = true;
      },
      error: (error: any) => {
        console.log(error);
      }
    });
  }

  

}