import {Component, OnInit} from '@angular/core';
import {GameSvcService} from "../game-svc.service";
// import { Http, Response, Headers, RequestOptions } from '@angular/http';

@Component({
  selector: 'app-game-boards',
  templateUrl: './game-boards.component.html',
  styleUrls: ['./game-boards.component.css']
})
export class GameBoardsComponent implements OnInit {

  constructor(private gameService : GameSvcService) {
  }

  board_1 = [];

  ngOnInit() {
    for (var h = 0; h < 10; h++) {
      var row = [];
      for (var w = 0; w < 10; w++) {
        // console.log("[" + h + ":" + w + "]");
        row.push(this.buildSquare());
      }
      this.board_1.push(row);
    }
  }


  buildShip() {
    return {
      id: "123abcdfkjfkgkjs",
      killed: false,
      type: "SUBMARINE",
      squares: [],
    }
  }

  buildSquare() {
    return {
      id: "kjfnbfjdsvnjdkgko35643dg",
      opened: false,
      ship: null,
    }
  }

  openSquare(square) {
    console.log(square);
    square.opened = true;
    square.ship = Math.random() % 3 == 1 ? null : this.buildShip();
    // square.ship = null;
  }

  getClassForOpponentSquare(square) {
    if (!square.opened) {
      return "closed";
    }

    if (square.ship) {
      if (square.ship.killed)
        return "opened_killed";
      return "opened_damaged";
    }
    return "opened_empty";
  }

  getClassForPlayerSquare(square) {
    if (!square.ship) {
      return "closed";
    }

    if (square.opened) {
      if (square.ship.killed)
        return "opened_killed";
      return "opened_damaged";
    }
    return "opened_healthy";
  }

}
