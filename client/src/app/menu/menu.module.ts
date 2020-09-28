import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MenuComponent } from './components/menu/menu.component';
import { MenuRoutingModule } from './menu-routing.module';

import { GameFacade } from '../facades/game.facade';
import {FormsModule} from '@angular/forms';

@NgModule({
  imports: [
    MenuRoutingModule,
    CommonModule,
    FormsModule
  ],
  declarations: [
    MenuComponent
  ],
  providers: [
    GameFacade
  ]
})
export class MenuModule { }
