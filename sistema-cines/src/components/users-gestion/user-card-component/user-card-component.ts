import { Component, Input } from "@angular/core";
import { Count } from "../../../models/Counts/count";
import { RouterLink } from "@angular/router";

@Component({
  selector: 'app-user-card',
  imports: [RouterLink],
  templateUrl: './user-card-component.html',
})
export class UserCardComponent {
  @Input({ required: true })
  user!: Count;
}