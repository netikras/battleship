import {Component, OnInit} from '@angular/core';
import {GameSvcService} from "../game-svc.service";
import {Match} from "../model/Match";
import {Square} from "../model/Square";
import {Board} from "../model/Board";
import {Coords} from "../model/Coords";
import {Ship} from "../model/Ship";
// import { Http, Response, Headers, RequestOptions } from '@angular/http';

@Component({
  selector: 'app-game-boards',
  templateUrl: './game-boards.component.html',
  styleUrls: ['./game-boards.component.css']
})
export class GameBoardsComponent implements OnInit {

  constructor(private gameService : GameSvcService) {}

  private match : Match;
  private errorMessage: string;

  private squaresA : Square[][];
  private squaresB : Square[][];

  ngOnInit() {}


  private sortSquares(match: Match): void {
    if (! match) {
      this.squaresA = null;
      this.squaresB = null;
      return;
    }

    let squares: Square[];
    console.log(match);

    squares = match.playerA.board.squares;
    this.squaresA = [];
    for (let i=0; i<squares.length; i++) {
      let coord: Coords = squares[i].coordinates;
      if (!this.squaresA[coord.x])
        this.squaresA[coord.x] = [];
      this.squaresA[coord.x][coord.y] = squares[i];
    }

    squares = match.playerB.board.squares;
    this.squaresB = [];
    for (let i=0; i<squares.length; i++) {
      let coord: Coords = squares[i].coordinates;
      if (!this.squaresB[coord.x])
        this.squaresB[coord.x] = [];
      this.squaresB[coord.x][coord.y] = squares[i];
    }

    this.match = match;
  }

  startNewMatch() {
    this.gameService.startNewMatch().subscribe(
      match => this.sortSquares(match),
      error => this.errorMessage = <any>error
    )

  }


  openSquare(square: Square) {
    console.log("Opening square:");
    console.log(square);
    square.isRevealed = true;
    let ship: Ship = this.getShip(square.shipId);
    if (ship) {
      ship.killed = true;
      for (let i=0; i<ship.squareIds.length; i++) {
        let shipSquare : Square = this.getSquare(ship.squareIds[i]);
        if (!shipSquare.isRevealed) {
          ship.killed = false;
          break;
        }
      }
    }
  }

  getClassForOpponentSquare(square : Square) {
    if (!square.isRevealed) {
      return "closed";
    }
    console.log(square);
    if (square.shipId) {
      if (this.getShip(square.shipId).killed)
        return "opened_killed";
      return "opened_damaged";
    }
    return "opened_empty";
  }

  getClassForPlayerSquare(square: Square) {
    if (!square.shipId) {
      return "closed";
    }

    if (square.isRevealed) {
      if (this.getShip(square.shipId).killed)
        return "opened_killed";
      return "opened_damaged";
    }
    return "opened_healthy";
  }

  getShip(shipId: string) : Ship {
    let ship: Ship;

    if (!this.match) {
      return null;
    }

    ship = this.getShipInBoard(this.match.playerA.board.boardId, shipId);
    if (ship) {
      return ship;
    }

    ship = this.getShipInBoard(this.match.playerB.board.boardId, shipId);
    if (ship) {
      return ship;
    }

    return null;
  }

  getShipInBoard(boardId: string, shipId: string) : Ship {
    if (! this.match) {
      return null;
    }

    let board: Board;
    board = this.match.playerA.board;
    if (!board || board.boardId !== boardId) {
      board = this.match.playerB.board;
      if (!board || board.boardId !== boardId) {
        return null;
      }
    }

    for (let i=0; i<board.ships.length; i++) {
      let ship : Ship = board.ships[i];
      if (ship.id === shipId) {
        return ship;
      }
    }

    return null;
  }

  getSquare(squareId: string) : Square {
    let square: Square;

    if (!this.match) {
      return null;
    }

    square = this.getSquareInBoard(this.match.playerA.board.boardId, squareId);
    if (square) {
      return square;
    }

    square = this.getSquareInBoard(this.match.playerB.board.boardId, squareId);
    if (square) {
      return square;
    }

    return null;
  }


  getSquareInBoard(boardId: string, squareId: string) : Square {
    if (!this.match) {
      return null;
    }

    let square: Square;

    let board: Board;
    board = this.match.playerA.board;
    if (!board || board.boardId !== boardId) {
      board = this.match.playerB.board;
      if (!board || board.boardId !== boardId) {
        return null;
      }
    }

    for (let i=0; i<board.squares.length; i++) {
      square = board.squares[i];
      if (square.id === squareId) {
        return square;
      }
    }

    return null;
  }

}
