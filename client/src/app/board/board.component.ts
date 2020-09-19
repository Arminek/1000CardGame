import { Component, OnInit, OnDestroy } from '@angular/core';
import { Store } from '@ngrx/store';
import * as GameSelectors from '../store/reducers/game.reducer';

@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.scss']
})
export class BoardComponent implements OnInit, OnDestroy {
  playersIds$ = this.store.select(GameSelectors.selectPlayersIds);
  playersIds: any;
  bids$ = this.store.select(GameSelectors.selectBids);
  bids: any;

  constructor(private readonly store: Store) {}

  ngOnInit(): void {
    this.playersIds$.subscribe(state => {
      this.playersIds = state;
    });
    this.bids$.subscribe(state => {
      this.bids = state;
    });
  }

  ngOnDestroy(): void {

  }

  playerBid(playerId): number {
    const playerBid = this.bids.find(player => player.playerId === playerId);

    return playerBid?.amount || 0;
  }
}
