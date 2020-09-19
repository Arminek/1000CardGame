import {Component, OnInit} from '@angular/core';
import { SseService } from './services/sse.service';
import { Store } from '@ngrx/store';
import { dealStarted, dealWasBid } from './store/actions/game.actions';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'client';
  logs: Array<any>;

  constructor(
    private sseService: SseService,
    private store: Store
  ) {}

  ngOnInit(): void {
    this.logs = [];

    this.sseService
      .getServerSentEvent('http://localhost:8080/v1/events')
      .subscribe(event => {
          const data = JSON.parse(event.data);
          this.handleLogs(data);
          switch (data.type) {
            case 'deal-started': {
              this.store.dispatch(dealStarted({data}));
              break;
            }
            case 'deal-was-bid': {
              this.store.dispatch(dealWasBid({data}));
              break;
            }
          }
        }
      );
  }

  handleLogs(log): void {
    if (this.logs.length > 5) {
      this.logs.shift();
    }
    this.logs.push(log);
  }
}
