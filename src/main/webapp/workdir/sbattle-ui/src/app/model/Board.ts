import {Square} from "./Square";
import {Ship} from "./Ship";

export class Board {
  boardId: string;
  createdOn: number;
  playerId: string;
  squares: Square[];
  ships: Ship[];

  constructor() {}
}
