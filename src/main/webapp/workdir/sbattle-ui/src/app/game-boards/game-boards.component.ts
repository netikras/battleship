import {Component, OnInit} from "@angular/core";
import {GameSvcService} from "../game-svc.service";
import {Match} from "../model/Match";
import {Square} from "../model/Square";
import {Board} from "../model/Board";
import {Coords} from "../model/Coords";
import {Ship} from "../model/Ship";
import {Observable} from "rxjs/Observable";

@Component({
  selector: 'app-game-boards',
  templateUrl: './game-boards.component.html',
  styleUrls: ['./game-boards.component.css']
})
export class GameBoardsComponent implements OnInit {

  private gameStarted: boolean = false;

  private setGameStarted(started: boolean): any {
    this.gameStarted = started;
  }

  constructor(private gameService: GameSvcService) {
  }

  private match: Match;
  private anchorSquare: Square;
  private availableShips: Ship[];
  private selectedSquares: Square[];
  private errorMessage: string;

  private squaresA: Square[][]; // [y][x]
  private squaresB: Square[][]; // [y][x]

  ngOnInit() {
  }


  private selectShipSquares(square: Square) {
    if (!this.anchorSquare) {
      this.unmarkAllSquares();
      this.anchorSquare = square;
      console.log("Setting anchor square:");
      console.log(square);
      return;
    }

    if (!this.selectedSquares) {
      this.unmarkAllSquares();
    }

    if (square.shipId) {
      this.unselectShipSquares(square);
      this.unmarkAllSquares();
      this.anchorSquare = null;
      return
    }

    console.log("Selected squares:");
    console.log(this.selectedSquares);

    let proposedShip: Ship = this.findAvailableShipByLength(this.selectedSquares.length);

    if (proposedShip) {
      for (let i = 0; i < this.selectedSquares.length; i++) {
        this.selectedSquares[i].shipId = proposedShip.id;
        if (!proposedShip.squareIds) proposedShip.squareIds = [];
        proposedShip.squareIds.push(this.selectedSquares[i].id);
      }
    }

    if (this.availableShips) {
      for (let i = this.availableShips.length - 1; i>=0; i--) {
        if (this.availableShips[i].id === proposedShip.id) {
          this.availableShips.splice(i, 1);
        }
      }
    }

    this.unmarkAllSquares();
    this.anchorSquare = null;
  }

  private unselectShipSquares(square: Square) {
    if (!square) return;
    if (!square.shipId) return;

    let shipId: string = square.shipId;
    let ship: Ship;
    let ships: Ship[] = this.match.playerA.board.ships;

    for (let i = 0; i < ships.length; i++) {
      ship = ships[i];
      if (ship.id === shipId) {
        this.availableShips.push(ship);
        this.unlinkShipFromSquares(ship);
      }
    }
  }

  private unlinkShipFromSquares(ship: Ship) {
    if (!ship) return;
    if (!ship.squareIds) return;

    let squares: Square[] = this.match.playerA.board.squares;

    for (let i = ship.squareIds.length - 1; i>=0; i--) {
      for (let s = 0; s < squares.length; s++) {
        if (squares[s].id === ship.squareIds[i]) {
          ship.squareIds.splice(i, 1);
          squares[s].shipId = null;
        }
      }
    }
  }

  private getShipLength(type: string) {
    switch (type) {
      case "CARRIER":
        return 5;
      case "BATTLESHIP":
        return 4;
      case "CRUISER" :
        return 3;
      case "DESTROYER" :
        return 2;
      case "SUBMARINE" :
        return 1;
    }
    return 0;
  }

  private getShipCount(type: string) {
    switch (type) {
      case "CARRIER":
        return 1;
      case "BATTLESHIP":
        return 1;
      case "CRUISER" :
        return 1;
      case "DESTROYER" :
        return 2;
      case "SUBMARINE" :
        return 2;
    }
    return 0;
  }

  private findAvailableShipByLength(len: number): Ship {

    let bestMatch = null;
// console.log("Looking for a ship of length: " + len + ". Available ships: " + this.availableShips.length);

    if (this.availableShips) {
      let lowestSizeDiff;
      for (let i = 0; i < this.availableShips.length; i++) {
        let ship: Ship = this.availableShips[i];
        let shipLen: number = this.getShipLength(ship.type);
        // console.log("Ship type " + ship.type + " length is " + shipLen);
        if (shipLen == len) {
          // console.log("Found exact ship length match:");
          // console.log(ship);
          return ship;
        }

        // If exact match is not found -- try finding the next best item
        // i.e. a ship which has length closest to required length
        if (!bestMatch) {
          bestMatch = ship;
          lowestSizeDiff = this.getShipLength(ship.type) - len;
          if (lowestSizeDiff < 0) lowestSizeDiff = lowestSizeDiff * (-1);
          continue;
        }

        let possibleLowestSizeDiff = this.getShipLength(bestMatch.type) - len;
        if (possibleLowestSizeDiff < 0) possibleLowestSizeDiff = possibleLowestSizeDiff * (-1);

        if (possibleLowestSizeDiff < lowestSizeDiff) {
          lowestSizeDiff = possibleLowestSizeDiff;
          bestMatch = ship;
        }

      }
    } else {
      console.log("NO SHIPS AVAILABLE");
    }

    return bestMatch;
  }

  private unmarkAllSquares() {
    this.selectedSquares = [];
  }

  private isSquareMarked(square: Square): boolean {
    // console.log("IS MARKED???");
    // console.log(square);
    // console.log("from list:");
    // console.log(this.selectedSquares);
    if (this.selectedSquares) {
      for (let i = 0; i < this.selectedSquares.length; i++) {
        // console.log("Is " + this.selectedSquares[i].id + " == " + square.id + " ?");
        if (this.selectedSquares[i].id === square.id) {
          // console.log("YES!");
          return true;
        }
        // console.log("NO!");
      }
    } else {
      console.log("NO SQUARES SELECTED TO BE MARKED");
    }
    return false;
  }

  private markUntilSquare(square: Square) {
    // console.warn("markUntilSquare");
    // console.warn(square);
    if (!this.anchorSquare) return;
    let len = 0;
    let shipLen = 0;
    let x_anch = this.anchorSquare.coordinates.x;
    let y_anch = this.anchorSquare.coordinates.y;
    let x_sq = square.coordinates.x;
    let y_sq = square.coordinates.y;

    let squareToMark: Square;
    let temporaryMarkedSquares: Square[] = [];

    // this.unmarkAllSquares();

    let matchingShip: Ship;

    // console.log("Marking until square:");
    // console.log(square);

    if (x_anch == x_sq) { // if on the same column
      console.log("VERTICAL");
      len = y_sq - y_anch;
      if (len < 0) len = 0 - len;
      len++;
      matchingShip = this.findAvailableShipByLength(len);
      if (!matchingShip) {
        return; // no more ships available
      }
      shipLen = this.getShipLength(matchingShip.type);

      console.log("length:" + len);
      console.log("shipLength:" + shipLen);

      if (y_anch < y_sq) { // if below anchor
        console.log("DOWN");
        if (y_anch + shipLen >= this.squaresA.length) {
          console.error("Cannot fit ship in the board following that direction");
          this.unmarkAllSquares();
          return;
        }
        y_sq = y_anch + shipLen - 1;
        for (let y = y_anch; y <= y_sq; y++) {
          squareToMark = this.squaresA[y][x_anch];
          if (squareToMark.shipId) {
            this.unmarkAllSquares();
            return;
          }
          temporaryMarkedSquares.push(squareToMark);
        }

      } else {
        console.log("UP");
        if (y_anch - shipLen < 0) {
          console.error("Cannot fit ship in the board following that direction");
          this.unmarkAllSquares();
          return;
        }
        y_sq = y_anch - shipLen + 1;
        for (let y = y_anch; y >= y_sq; y--) {
          squareToMark = this.squaresA[y][x_anch];
          if (squareToMark.shipId) {
            this.unmarkAllSquares();
            return;
          }
          temporaryMarkedSquares.push(squareToMark);
        }

      }

    } else if (y_anch == y_sq) { // if on the same row
      console.log("HORIZONTAL");
      len = x_sq - x_anch;
      if (len < 0) len = 0 - len;
      matchingShip = this.findAvailableShipByLength(len);
      if (!matchingShip) {
        return; // no more ships available
      }
      shipLen = this.getShipLength(matchingShip.type);

      if (x_anch < x_sq) { // if on the left of anchor
        console.log("LEFT");
        if (x_anch + shipLen >= this.squaresA.length) {
          console.error("Cannot fit ship in the board following that direction");
          this.unmarkAllSquares();
          return;
        }
        x_sq = x_anch + shipLen - 1;
        for (let x = x_anch; x <= x_sq; x++) {
          squareToMark = this.squaresA[y_anch][x];
          if (squareToMark.shipId) {
            this.unmarkAllSquares();
            return;
          }
          temporaryMarkedSquares.push(squareToMark);
        }
      } else {
        console.log("RIGHT");
        if (x_anch - shipLen < 0) {
          console.error("Cannot fit ship in the board following that direction");
          this.unmarkAllSquares();
          return;
        }
        x_sq = x_anch - shipLen + 1;
        for (let x = x_anch; x >= x_sq; x--) {
          squareToMark = this.squaresA[y_anch][x];
          if (squareToMark.shipId) {
            this.unmarkAllSquares();
            return;
          }
          temporaryMarkedSquares.push(squareToMark);
        }
      }
    }

    // console.log("Selected squares:");
    // console.log(temporaryMarkedSquares);
    this.selectedSquares = temporaryMarkedSquares;

  }

  private sortSquares(match: Match): void {
    if (!match) {
      this.squaresA = null;
      this.squaresB = null;
      return;
    }

    let squares: Square[];
    console.log(match);

    squares = match.playerA.board.squares;
    this.squaresA = [];
    for (let i = 0; i < squares.length; i++) {
      let coord: Coords = squares[i].coordinates;
      if (!this.squaresA[coord.y])
        this.squaresA[coord.y] = [];
      this.squaresA[coord.y][coord.x] = squares[i];
    }

    squares = match.playerB.board.squares;
    this.squaresB = [];
    for (let i = 0; i < squares.length; i++) {
      let coord: Coords = squares[i].coordinates;
      if (!this.squaresB[coord.y])
        this.squaresB[coord.y] = [];
      this.squaresB[coord.y][coord.x] = squares[i];
    }

    this.match = match;

    let ship: Ship;
    this.availableShips = [];
    this.unmarkAllSquares();
    for (let i=0; i<match.playerA.board.ships.length; i++) {
      ship = match.playerA.board.ships[i];
      this.availableShips.push(ship);
      this.unlinkShipFromSquares(ship);
    }
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
      for (let i = 0; i < ship.squareIds.length; i++) {
        let shipSquare: Square = this.getSquare(ship.squareIds[i]);
        if (!shipSquare.isRevealed) {
          ship.killed = false;
          break;
        }
      }
    }
  }

  getClassForOpponentSquare(square: Square) {
    if (!square.isRevealed) {
      return "closed";
    }
    // console.log(square);
    if (square.shipId) {
      if (this.getShip(square.shipId).killed)
        return "opened_killed";
      return "opened_damaged";
    }
    return "opened_empty";
  }

  getClassForPlayerSquare(square: Square) {
    if (this.isSquareMarked(square)) {
      // console.log("prepare_marked");
      return "prepare_marked";
    }

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

  getShip(shipId: string): Ship {
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

  getShipInBoard(boardId: string, shipId: string): Ship {
    if (!this.match) {
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

    for (let i = 0; i < board.ships.length; i++) {
      let ship: Ship = board.ships[i];
      if (ship.id === shipId) {
        return ship;
      }
    }

    return null;
  }

  getSquare(squareId: string): Square {
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


  getSquareInBoard(boardId: string, squareId: string): Square {
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

    for (let i = 0; i < board.squares.length; i++) {
      square = board.squares[i];
      if (square.id === squareId) {
        return square;
      }
    }

    return null;
  }

  private startGame() {
    let playerBoard: Board = this.match.playerA.board;
    this.gameService.submitShips(playerBoard.boardId, playerBoard.ships).subscribe(
      result => this.setGameStarted(result),
      error => this.errorMessage = <any>error
    );

  }

}
