import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Ticket} from './ticket';

@Injectable()
export class TicketService {
  private ticketURL: string;

  constructor(private http: HttpClient) {
    this.ticketURL = 'http://localhost:8080/api/tickets';
  }

  public findAll(): Observable<Ticket[]> {
    return this.http.get<Ticket[]>(this.ticketURL);
  }
}
