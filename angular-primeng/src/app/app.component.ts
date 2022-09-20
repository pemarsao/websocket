import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { Information } from './model/model';
import { WebSocketConnector } from './websocket/websocket-connector';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {

  notifications = 0;
  show = false;
  items: MenuItem[] = [];
  informations: Information[] = [];

  ngOnInit() {
      this.items = [
          {
              label: 'Update',
              icon: 'pi pi-refresh'
          },
          {
              label: 'Delete',
              icon: 'pi pi-times'
          },
          {
              label: 'Angular',
              icon: 'pi pi-external-link',
              url: 'http://angular.io'
          },
          {
              label: 'Router',
              icon: 'pi pi-upload',
              routerLink: '/fileupload'
          }
      ];
  }

  isNotification(): Boolean {
    return this.notifications > 0;
  }

  clickMethod() {
    this.show = !this.show
    this.notifications = 0
  }

  private webSocketConnector: WebSocketConnector = new WebSocketConnector(
    'http://localhost:8080/socket',
    '/statusProcessor',
    this.onMessage.bind(this)
  );

  constructor(private http: HttpClient) {
  }

  start() {
    this.http.put('http://localhost:8080/api', {})
      .subscribe(response => console.log(response));
  }

  onMessage(message: any): void {
    this.informations = JSON.parse(message.body);
    console.log(message)
    if (!this.show) {
      this.notifications = this.informations.length
    } else {
      this.notifications = 0
    }
    
  }

  cleanLogs() {
    this.notifications = 0
    this.informations = []
  }

}
