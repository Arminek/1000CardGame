import {createAction, props} from '@ngrx/store';

export const dealStarted = createAction(
  '[Game][Server] Event received - deal-started',
  props<{ data: any }>()
);

export const dealWasBid = createAction(
  '[Game][Server] Event received - deal-was-bid',
  props<{ data: any }>()
);
