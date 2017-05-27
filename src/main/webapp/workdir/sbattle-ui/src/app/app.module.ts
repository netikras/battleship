import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule, JsonpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { GameBoardsComponent } from './game-boards/game-boards.component';
import { GameStatsComponent } from './game-stats/game-stats.component';
import { GameSvcService } from './game-svc.service'
import {TooltipService} from "./tooltip.service";
import { GameTooltipsComponent } from './game-tooltips/game-tooltips.component';
import {StatsService} from "./stats.service";

@NgModule({
  declarations: [
    AppComponent,
    GameBoardsComponent,
    GameStatsComponent,
    GameTooltipsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule
  ],
  providers: [
    GameSvcService,
    TooltipService,
    StatsService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
