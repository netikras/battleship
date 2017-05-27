import {Injectable} from '@angular/core';

@Injectable()
export class TooltipService {

  constructor() {
  }

  private enabled_modes: string[] = [];

  public modes = {

    ships_A: {
      enabled: false,
      values: []
    },

    ships_B: {
      enabled: false,
      values: []
    },

    current_square: {
      enabled: false,
      values: []
    },

  };


}
