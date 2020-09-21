import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  constructor(private http: HttpClient) {}

  postGames(): Promise<any> {
    return this.http.post<any>(environment.url, {}).toPromise().then(response => response);
  }
}
