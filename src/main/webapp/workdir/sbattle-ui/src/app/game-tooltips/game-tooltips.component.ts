import { Component, OnInit } from '@angular/core';
import {TooltipService} from "../tooltip.service";

@Component({
  selector: 'app-game-tooltips',
  templateUrl: './game-tooltips.component.html',
  styleUrls: ['./game-tooltips.component.css']
})
export class GameTooltipsComponent implements OnInit {

  constructor(private tooltipService: TooltipService) { }

  ngOnInit() {
  }


  isEnabled(modename: string): boolean {
    return this.rawmode(modename).enabled;
  }

  rawmode(modename: string): any {
    return this.tooltipService.modes[modename];
  }

  modenames() : Array<string> {
    return Object.keys(this.tooltipService.modes);
  }

}
