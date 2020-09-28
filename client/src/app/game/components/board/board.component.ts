import {Component, Input, Output, EventEmitter} from '@angular/core';


@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.scss']
})
export class BoardComponent {
  @Input() bidAmount: number;
  @Input() playersIds: Array<string>;
  @Input() currentPlayerId: string;
  @Output() increaseBidEmit = new EventEmitter<number>();

  constructor() {}

  increaseBid(amount: number): void {
    this.increaseBidEmit.emit(amount);
  }
}
