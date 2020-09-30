import {Component, Input, Output, EventEmitter, OnInit} from '@angular/core';


@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.scss']
})
export class BoardComponent implements OnInit {
  declaredAmount: number;
  @Input() bidAmount: number;
  @Input() playersIds: Array<string>;
  @Input() currentPlayerId: string;
  @Input() currentBid: any;
  @Output() increaseBidEmit = new EventEmitter<number>();
  @Output() passBidEmit = new EventEmitter<number>();
  @Output() declareBidEmit = new EventEmitter<number>();

  ngOnInit(): void {
    this.declaredAmount = 0;
  }

  increaseBid(amount: number): void {
    this.increaseBidEmit.emit(amount);
  }

  passBid(): void {
    this.passBidEmit.emit();
  }

  declareBid(): void {
    this.declareBidEmit.emit(this.declaredAmount);
  }
}
