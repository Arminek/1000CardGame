import {createAction, props} from '@ngrx/store';

export const gameStarted = createAction(
  '[Game][Server] Post received - Created',
  props<{ payload: any }>()
);

export const gameCreated = createAction(
  '[Game][Server] Event received - game-created',
  props<{ payload: any }>()
);

export const dealWasBid = createAction(
  '[Game][Server] Event received - deal-was-bid',
  props<{ data: any }>()
);
