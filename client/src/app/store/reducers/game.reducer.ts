import { createReducer, on, createFeatureSelector, createSelector } from '@ngrx/store';
import { dealStarted, dealWasBid } from '../actions/game.actions';

export interface GameState {
  playerIds: any;
  bids: Array<any>;
}

export const initialState: GameState = {
  playerIds: null,
  bids: [],
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

export const GameStateReducer = createReducer(
  initialState,
  on(dealStarted, (state, action ) => {
    return {
      ...state,
      playerIds: action.data.playerIds
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
