import {Injectable} from '@angular/core';
import {Match} from "./model/Match";

@Injectable()
export class StatsService {

  constructor() {
  }

  private match: Match;

  public setMatch(m: Match) {
    this.match = m;
  }

  public getMatch() {
    return this.match;
  }
}
