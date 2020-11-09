import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {getEventSource, closeEventSource} from './event-source';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class GameService {

  constructor(private http: HttpClient) {
  }

  postGames(): Promise<any> {
    return this.http.post<any>(`${environment.url}/v1/games`, {}).toPromise().then(response => response);
  }

  postIncreaseBid(id: string, amount: number): Promise<any> {
    return this.http.post<any>(`${environment.url}/v1/games/${id}/bid`, {amount}).toPromise().then(response => response);
  }

  deletePassBid(id: string): Promise<any> {
    return this.http.delete<any>(`${environment.url}/v1/games/${id}/bid`).toPromise().then(response => response);
  }

  postDeclareBid(id: string, amount: number): Promise<any> {
    return this.http.post<any>(`${environment.url}/v1/games/${id}/declare`, {amount}).toPromise().then(response => response);
  }

  get(url: string, options: {
    withCredentials?: boolean,
    /**
     * Complete the observable after a period of time automatically
     */
    duration?: number,
    /**
     * If set to true, keep event source open (i.e. will not close and keep it in a event source pool for reuse) after unsubscribing
     */
    keepOpenWhenUnsubscribe?: boolean,
  } = {}): Observable<any> {
    const subject = new Observable<any>(subscriber => {
      const sse = getEventSource(environment.url + url, options);
      if (!sse) {
        return subscriber.error(new Error(`failed to init EventSource for url ${url}`));
      }

      let stopped = false;

      /**
       * Stop listening to event source,
       * close and release it if options.keepOpenWhenUnsubscribe is set to falsy
       */
      const stop = () => {
        console.log('stop function entered');
        if (stopped) {
          return;
        }
        stopped = true;

        sse.removeEventListener('message', onMessage);
        sse.removeEventListener('error', onError);

        if (options.keepOpenWhenUnsubscribe !== true) {
          closeEventSource(url);
        }
      };

      const onMessage = (message: MessageEvent) => {
        const {data} = message;
        try {
          const json = JSON.parse(data);
          subscriber.next(json);
        } catch (error) {
          subscriber.next(data);
        }
      };

      const onError = (message: MessageEvent) => {
        // unsubscribe will be called after then
        subscriber.error(message);
      };

      sse.addEventListener('message', onMessage);
      sse.addEventListener('error', onError);

      if (typeof options.duration === 'number') {
        setTimeout(() => {
          // unsubscribe will be called after then
          subscriber.complete();
        }, options.duration);
      }

      return {
        unsubscribe(): void {
          stop();
        }
      };
    });

    return subject;
  }
}
