import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-logs',
  templateUrl: './logs.component.html',
  styleUrls: ['./logs.component.scss']
})
export class LogsComponent implements OnInit {

  @Input() logs: any;
  constructor() {}

  ngOnInit(): void {

  }

  showLog(log: any): string {
    return JSON.stringify(log);
  }
}
