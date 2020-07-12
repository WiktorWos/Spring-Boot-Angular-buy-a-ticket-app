import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { TicketListComponent } from './ticket-list/ticket-list.component';
import {HttpClientModule} from '@angular/common/http';
import {TicketService} from './ticket.service';

@NgModule({
  declarations: [
    AppComponent,
    TicketListComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [TicketService],
  bootstrap: [AppComponent]
})
export class AppModule { }
