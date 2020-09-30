import { Component, OnInit, OnDestroy } from '@angular/core';
import { Store } from '@ngrx/store';
import { GameFacade } from '../../../facades/game.facade';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss']
})
export class GameComponent implements OnInit, OnDestroy {
  playersIds$: Subscription;
  bids$: Subscription;
  gameId$: Subscription;
  currentBidId$: Subscription;
  currentPlayerId$: Subscription;
  logs$: Subscription;
  playersIds: any;
  bids: Array<any>;
  gameId: string;
  currentBidId: string;
  currentPlayerId: string;
  logs: Array<string>;

  constructor(
    private readonly store: Store,
    private gameFacade: GameFacade,
  ) {}

  ngOnInit(): void {
    this.playersIds$ = this.playersIdSubscription();
    this.bids$ = this.bidsSubscription();
    this.gameId$ = this.gameIdSubscription();
    this.currentBidId$ = this.currentBidIdSubscription();
    this.currentPlayerId$ = this.currentPlayerIdSubscription();
    this.logs$ = this.logsSubscription();
    this.gameFacade.startGameEvent();
  }

  ngOnDestroy(): void {
    this.gameFacade.closeGameEvent();
    this.playersIds$.unsubscribe();
    this.bids$.unsubscribe();
    this.gameId$.unsubscribe();
    this.currentPlayerId$.unsubscribe();
    this.logs$.unsubscribe();
  }

  currentPlayerIdSubscription(): Subscription {
    return this.gameFacade.selectCurrentPlayerId().subscribe(currentPlayerId => {
      this.currentPlayerId = currentPlayerId;
    });
  }

  playersIdSubscription(): Subscription {
    return this.gameFacade.selectPlayersId().subscribe(playersIds => {
      this.playersIds = playersIds;
    });
  }

  bidsSubscription(): Subscription {
    return this.gameFacade.selectBids().subscribe(bids => {
      this.bids = bids;
    });
  }

  gameIdSubscription(): Subscription {
    return this.gameFacade.selectGameId().subscribe(gameId => {
      this.gameId = gameId;
    });
  }

  currentBidIdSubscription(): Subscription {
    return this.gameFacade.selectCurrentBidId().subscribe(currentBidId => {
      this.currentBidId = currentBidId;
    });
  }

  logsSubscription(): Subscription {
    return this.gameFacade.selectLogs().subscribe(logs => {
      this.logs = logs;
    });
  }

  increaseBid(amount: number): void {
    this.gameFacade.increaseBid(amount);
  }

  passBid(): void {
    this.gameFacade.passBid();
  }

  currentBid(): any {
    return this.bids[this.currentBidId];
  }

  declareBid($event): void {
    this.gameFacade.declareBid($event);
  }

  playerBid(): number {
    const bidEntities = this.bids[this.currentBidId];
    if (!bidEntities) {
      return  0;
    }
    return bidEntities.entities.reduce((initial, event) => {
      return initial + event.amount;
    }, 0);
  }
}
