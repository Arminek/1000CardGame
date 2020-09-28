import {createAction, props} from '@ngrx/store';

export const gameStarted = createAction(
  '[Game] Post received - created',
  props<{ payload: any }>()
);

export const gameCreated = createAction(
  '[Game] Event received - game-created',
  props<{ event: any }>()
);

export const addLog = createAction(
  '[Game][addLog] Event received',
  props<{ message: string }>()
);


// bidding
export const dealWasBid = createAction(
  '[Game] Event received - deal-was-bid',
  props<{ data: any }>()
);

export const bidStarted = createAction(
  '[Game] Event received - bid-started',
  props<{ event: any }>()
);

export const bidIncreased = createAction(
  '[Game] Event received - bid-increased',
  props<{ event: any }>()
);

export const turnStarted = createAction(
  '[Game] Event received - turn-started',
  props<{ event: any }>()
);
