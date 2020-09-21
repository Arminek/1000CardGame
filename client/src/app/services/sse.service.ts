import { Injectable, NgZone } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SseService {
  constructor(private zone: NgZone) {}

  getServerSentEvent(url: string): Observable<any> {
    return Observable.create(observer => {
      const eventSource = this.getEventSource(`${environment.url}/${url}`);
      eventSource.onmessage = event => {
        this.zone.run(() => {
          observer.next(event);
        });
      };
      eventSource.onerror = error => {
        this.zone.run(() => {
          observer.error(error);
        });
        eventSource.close();
      };
    });
  }

  private getEventSource(url: string): EventSource {
    return new EventSource(url);
  }
}
