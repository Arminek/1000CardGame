import { NgModule } from '@angular/core';

import { BoardComponent } from './board.component';
import { BoardRoutingModule } from './board-routing.module';
import {CommonModule} from '@angular/common';

@NgModule({
  imports: [BoardRoutingModule, CommonModule],
  declarations: [
    BoardComponent
  ],
})
export class BoardModule { }
