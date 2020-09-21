import {ActionReducerMap} from '@ngrx/store';

import { GameStateReducer, GameState } from './game.reducer';

export interface State {
  game: GameState;
}

export const reducers: ActionReducerMap<State> = {
  game: GameStateReducer
};
