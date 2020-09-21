import { createReducer, on, createFeatureSelector, createSelector } from '@ngrx/store';
import { dealWasBid, gameCreated, gameStarted } from '../actions/game.actions';

export interface GameState {
  playerIds: any;
  bids: Array<any>;
  id: string;
}

export const initialState: GameState = {
  playerIds: null,
  bids: [],
  id: '',
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

export const GameStateReducer = createReducer(
  initialState,
  on(gameStarted, (state, {payload} ) => {
    return {
      ...state,
      id: payload.id
    };
  }),
  on(gameCreated, (state, {payload} ) => {
    return {
      ...state,
      playerIds: payload.playerIds
    };
  }),
  on(dealWasBid, (state, action ) => {
    const playerHasBid = state.bids.findIndex(bid => bid.playerId === action.data.playerId);
    const allBids = JSON.parse(JSON.stringify(state.bids));

    if (playerHasBid !== -1) {
      allBids.splice(playerHasBid, 1);
    }
    allBids.push(action.data);

    return {
      ...state,
      bids: allBids
    };
  }),
);
