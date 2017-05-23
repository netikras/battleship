import {Player} from "./Player";

export class Match {
  id: string;
  createdOn: number;
  playerA: Player;
  playerB: Player;
  winnerId: string;

  constructor() {}
}
