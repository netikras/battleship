import {Component, OnInit} from '@angular/core';
import {StatsService} from "../stats.service";
import {Match} from "../model/Match";
import {Ship} from "../model/Ship";
import {Square} from "../model/Square";
import {Player} from "../model/Player";

@Component({
  selector: 'app-game-stats',
  templateUrl: './game-stats.component.html',
  styleUrls: ['./game-stats.component.css']
})
export class GameStatsComponent implements OnInit {

  public stats_on: boolean = false;
  public ext_on: boolean = false;

  constructor(private service: StatsService) {
  }

  ngOnInit() {
  }



  private getMatch(): Match {
    return this.service.getMatch();
  }

  private getShipsForA(): Ship[] {
    if (!this.getMatch()) {
      return null;
    }

    return this.getMatch().playerA.board.ships;
  }


  private getShipsForB(): Ship[] {
    if (!this.getMatch()) {
      return null;
    }

    return this.getMatch().playerB.board.ships;
  }


  private getSquaresForA(): Square[] {
    if (!this.getMatch()) {
      return null;
    }

    return this.getMatch().playerA.board.squares;
  }

  private getSquaresForB(): Square[] {
    if (!this.getMatch()) {
      return null;
    }

    return this.getMatch().playerB.board.squares;
  }


  getPlayerForA(): Player {
    if (!this.getMatch()) {
      return null;
    }

    return this.getMatch().playerA;
  }

  public getPlayerForB(): Player {
    if (!this.getMatch()) {
      return null;
    }

    return this.getMatch().playerB;
  }

  public getSquaresStatsForA(): string[] {
    let squares: Square[] = this.getPlayerForA().board.squares;
    let revealed: number = 0;
    let damagedShips: number = 0;

    for (let i=0; i<squares.length; i++) {
      if (squares[i].revealed) {
        revealed++;
        if (squares[i].shipId) damagedShips++;
      }
    }

    let shipsKilled: number = 0;
    let ships: Ship[] = this.getPlayerForA().board.ships;
    for (let i=0; i<ships.length; i++) {
      if (ships[i].killed) shipsKilled++;
    }

    return [
      "Opened squares: " + revealed + "/" + this.getPlayerForA().board.squares.length + "",
      "Ships drowned: " + shipsKilled + "/" + this.getPlayerForA().board.ships.length + "",
      "Ship squares damaged:" + damagedShips + "/" + 18 + ""
    ];
  }

  public getSquaresStatsForB(): string[] {
    let squares: Square[] = this.getPlayerForB().board.squares;
    let revealed: number = 0;
    let damagedShips: number = 0;

    for (let i=0; i<squares.length; i++) {
      if (squares[i].revealed) {
        revealed++;
        if (squares[i].shipId) damagedShips++;
      }
    }

    let shipsKilled: number = 0;
    let ships: Ship[] = this.getPlayerForB().board.ships;
    for (let i=0; i<ships.length; i++) {
      if (ships[i].killed) shipsKilled++;
    }

    return [
      "Opened squares: " + revealed + "/" + this.getPlayerForB().board.squares.length + "",
      "Ships drowned: " + shipsKilled + "/" + this.getPlayerForB().board.ships.length + "",
      "Ship squares damaged:" + damagedShips + "/" + 18 + ""
    ];
  }


}
