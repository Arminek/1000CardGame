import { Component, OnInit, OnDestroy } from '@angular/core';
import { Store } from '@ngrx/store';
import * as GameSelectors from '../store/reducers/game.reducer';
import { gameCreated } from '../store/actions/game.actions';
import { SseService } from '../services/sse.service';

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
  gameId$ = this.store.select(GameSelectors.selectGameId);
  gameId: any;

  constructor(
    private readonly store: Store,
    private sseService: SseService
  ) {}

  ngOnInit(): void {
    this.playersIds$.subscribe(state => {
      this.playersIds = state;
    });
    this.bids$.subscribe(state => {
      this.bids = state;
    });
    this.gameId$.subscribe(state => {
      this.gameId = state;
      this.sseService
        .getServerSentEvent(`${state}/events`)
        .subscribe(event => {
            const payload = JSON.parse(event.data);
            console.log(payload);
            switch (payload.type) {
              case 'game-created': {
                this.store.dispatch(gameCreated({payload}));
                break;
              }
              // case 'deal-was-bid': {
              //   this.store.dispatch(dealWasBid({payload}));
              //   break;
              // }
            }
          }
        );
    });
  }

  ngOnDestroy(): void {

  }

  playerBid(playerId): number {
    const playerBid = this.bids.find(player => player.playerId === playerId);

    return playerBid?.amount || 0;
  }
}
