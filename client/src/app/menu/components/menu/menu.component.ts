import { Component, OnInit, OnDestroy } from '@angular/core';
import { gameStarted } from '../../../actions/game.actions';
import { Store } from '@ngrx/store';
import { Router } from '@angular/router';
import {GameFacade} from '../../../facades/game.facade';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit, OnDestroy {
  constructor(
    private gameFacade: GameFacade,
    private store: Store,
    private router: Router,
  ) {}

  ngOnInit(): void {

  }

  ngOnDestroy(): void {

  }

  createGame(): void {
    this.gameFacade.createGame().then(payload => {
      this.store.dispatch(gameStarted({payload}));
      this.router.navigate(['/game']);
    });
  }
}
