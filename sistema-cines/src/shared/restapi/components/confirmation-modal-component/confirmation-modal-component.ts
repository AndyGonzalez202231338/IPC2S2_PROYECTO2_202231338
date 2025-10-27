import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Count } from '../../../../models/Counts/count';

@Component({
  selector: 'app-confirmation-modal-component',
  imports: [],
  templateUrl: './confirmation-modal-component.html',
  styleUrl: './confirmation-modal-component.css'
})
export class ConfirmationModalComponent {
  @Input()
  selectedUser!: Count;

  @Output()
  confirmationExecuter = new EventEmitter<void>();

  executeConfirmation(): void {
    this.confirmationExecuter.emit();
  }
}
