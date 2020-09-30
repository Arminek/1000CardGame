import {createReducer, on, createFeatureSelector, createSelector} from '@ngrx/store';
import {addLog, bidIncreased, bidStarted, bidWon, gameCreated, gameStarted, turnStarted} from '../actions/game.actions';

export interface GameState {
  playerIds: Array<string>;
  bids: any;
  id: string;
  currentBidId: string;
  currentPlayerId: string;
  logs: Array<string>;
}

export const initialState: GameState = {
  playerIds: [],
  bids: {},
  id: '',
  currentBidId: '',
  currentPlayerId: '',
  logs: [],
};

const selectGameState = createFeatureSelector<GameState>('game');

export const selectPlayersIds = createSelector(
  selectGameState,
  (state: GameState) => state.playerIds
);

export const selectBids = createSelector(
  selectGameState,
  (state: GameState) => state.bids
);

export const selectGameId = createSelector(
  selectGameState,
  (state: GameState) => state.id
);

export const selectCurrentBidId = createSelector(
  selectGameState,
  (state: GameState) => state.currentBidId
);

export const selectCurrentPlayerId = createSelector(
  selectGameState,
  (state: GameState) => state.currentPlayerId
);

export const selectLogs = createSelector(
  selectGameState,
  (state: GameState) => state.logs
);

export const GameStateReducer = createReducer(
  initialState,
  on(gameStarted, (state, {payload}) => {
    return {
      ...initialState,
      id: payload.id
    };
  }),
  on(gameCreated, (state, {event}) => {
    return {
      ...state,
      playerIds: event.playerIds
    };
  }),
  on(addLog, (state, {message}) => {
    return {
      ...state,
      logs: [...state.logs, message]
    };
  }),
  on(turnStarted, (state, {event}) => {
    return {
      ...state,
      currentPlayerId: event.playerId
    };
  }),
  on(bidStarted, (state, {event}) => {
    return {
      ...state,
      currentBidId: event.bidId,
      bids: Object.assign({}, state.bids, {[event.bidId]: {bidId: event.bidId, entities: [], won: false}})
    };
  }),
  on(bidIncreased, (state, {event}) => {
    return {
      ...state,
      bids: Object.assign({}, state.bids, {
        [event.bidId]: {
          bidId: event.bidId,
          entities: [...state.bids[event.bidId].entities, {playerId: event.playerId, amount: event.amount}]
        }
      })
    };
  }),
  on(bidWon, (state, {event}) => {
    return {
      ...state,
      bids: Object.assign({}, state.bids, {
        [event.bidId]: {
          ...state.bids[event.bidId],
          won: true
        }
      })
    };
  }),
);
