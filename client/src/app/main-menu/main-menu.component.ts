import { Component, OnInit, OnDestroy } from '@angular/core';
import { PostService } from '../services/post.service';
import { gameStarted } from '../store/actions/game.actions';
import { Store } from '@ngrx/store';
import { Router } from '@angular/router';

@Component({
  selector: 'app-main-menu',
  templateUrl: './main-menu.component.html',
  styleUrls: ['./main-menu.component.scss']
})
export class MainMenuComponent implements OnInit, OnDestroy {
  constructor(
    private postService: PostService,
    private store: Store,
    private router: Router,
  ) {}

  ngOnInit(): void {

  }

  ngOnDestroy(): void {

  }

  createGame(): void {
    this.postService.postGames().then(payload => {
      console.log(payload);
      this.store.dispatch(gameStarted({payload}));
      this.router.navigate(['/board']);
    });
  }
}
