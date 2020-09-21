import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {MainMenuComponent} from './main-menu/main-menu.component';

export const routes: Routes = [
  { path: '', component: MainMenuComponent, pathMatch: 'full'},
  { path: 'board', loadChildren: () => import('./board/board.module').then(m => m.BoardModule) },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
