import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {BoardComponent} from './components/board/board.component';
import {GameComponent} from './components/game/game.component';
import {LogsComponent} from './components/logs/logs.component';
import {GameRoutingModule} from './game-routing.module';

import {GameFacade} from '../facades/game.facade';
import {FormsModule} from '@angular/forms';

@NgModule({
  imports: [
    GameRoutingModule,
    CommonModule,
    FormsModule
  ],
  declarations: [
    GameComponent,
    BoardComponent,
    LogsComponent
  ],
  providers: [
    GameFacade
  ]
})
export class GameModule {
}
