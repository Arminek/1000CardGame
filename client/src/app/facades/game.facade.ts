import {Injectable, Injector} from '@angular/core';
import {GameService} from '../services/game.service';
import {Observable} from 'rxjs';
import {closeEventSource} from '../services/event-source';
import {environment} from '../../environments/environment';
import * as GameSelectors from '../reducers/game.reducer';
import {Store} from '@ngrx/store';
import {State} from '../reducers';
import {addLog, bidIncreased, bidStarted, bidWon, gameCreated, turnStarted} from '../actions/game.actions';
import {take} from 'rxjs/operators';

@Injectable()
export class GameFacade {
  private playersIds$ = this.store.select(GameSelectors.selectPlayersIds);

  private bids$ = this.store.select(GameSelectors.selectBids);

  private gameId$ = this.store.select(GameSelectors.selectGameId);

  private currentBidId$ = this.store.select(GameSelectors.selectCurrentBidId);

  private selectCurrentPlayerId$ = this.store.select(GameSelectors.selectCurrentPlayerId);

  private logs$ = this.store.select(GameSelectors.selectLogs);

  private gameService: GameService;

  public get getGameService(): GameService {
    if (!this.gameService) {
      this.gameService = this.injector.get(GameService);
    }
    return this.gameService;
  }

  constructor(
    private injector: Injector,
    private store: Store<State>
  ) {
  }

  selectPlayersId(): Observable<any> {
    return this.playersIds$;
  }

  selectBids(): Observable<any> {
    return this.bids$;
  }

  selectGameId(): Observable<any> {
    return this.gameId$;
  }

  selectCurrentBidId(): Observable<any> {
    return this.currentBidId$;
  }

  selectCurrentPlayerId(): Observable<any> {
    return this.selectCurrentPlayerId$;
  }

  selectLogs(): Observable<any> {
    return this.logs$;
  }

  createGame(): Promise<any> {
    return this.getGameService.postGames();
  }

  increaseBid(amount: number): void {
    this.selectGameId().pipe(take(1)).subscribe(gameId => {
      this.getGameService.postIncreaseBid(gameId, amount).then().catch(error => {
        console.log(error);
        this.store.dispatch(addLog({message: error.error.message}));
      });
    });
  }

  passBid(): void {
    this.selectGameId().pipe(take(1)).subscribe(gameId => {
      this.getGameService.deletePassBid(gameId).then().catch(error => {
        console.log(error);
        this.store.dispatch(addLog({message: error.error.message}));
      });
    });
  }

  declareBid(amount: number): void {
    this.selectGameId().pipe(take(1)).subscribe(gameId => {
      this.getGameService.postDeclareBid(gameId, amount).then().catch(error => {
        console.log(error);
        this.store.dispatch(addLog({message: error.error.message}));
      });
    });
  }

  startGameEvent(): void {
    this.selectGameId().pipe(take(1)).subscribe(gameId => {
      this.getGameService.get(`${gameId}/events`, {keepOpenWhenUnsubscribe: true}).subscribe(event => {
          console.log(event);
          let message = '';
          switch (event.type) {
            case 'game-created': {
              this.store.dispatch(gameCreated({event}));
              break;
            }
            case 'bid-started': {
              this.store.dispatch(bidStarted({event}));
              break;
            }
            case 'bid-increased': {
              this.store.dispatch(bidIncreased({event}));
              message = `Player#${event.playerId.split('-')[0]} [${event.type}] by ${event.amount}`;
              break;
            }
            case 'turn-started': {
              this.store.dispatch(turnStarted({event}));
              break;
            }
            case 'bid-passed': {
              message = `Player#${event.playerId.split('-')[0]} [${event.type}]`;
              break;
            }
            case 'bid-won': {
              this.store.dispatch(bidWon({event}));
              message = `Player#${event.playerId.split('-')[0]} [${event.type}] won`;
              break;
            }
            case 'bid-declared': {
              message = `Player#${event.playerId.split('-')[0]} [${event.type}] by ${event.amount}`;
              break;
            }
          }
          if (message) {
            this.store.dispatch(addLog({message}));
          }
        }
      );
    });
  }

  closeGameEvent(): void {
    this.selectGameId().pipe(take(1)).subscribe(gameId => {
      closeEventSource(`${environment.url}${gameId}/events`);
    });
  }
}
