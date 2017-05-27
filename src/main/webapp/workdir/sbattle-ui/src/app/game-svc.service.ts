import { Injectable }              from '@angular/core';
import { Http, Response }          from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';

import { Headers, RequestOptions } from '@angular/http';

import {Ship} from './model/Ship'
import {Square} from './model/Square'
import {Player} from './model/Player'
import {Board} from './model/Board'
import {Coords} from './model/Coords'
import {Match} from './model/Match'


@Injectable()
export class GameSvcService {

  constructor(private http : Http) { }

  private baseUrl : string = "http://localhost:8080/sbattle/game/match/";

  private matchId: string = "0";

  private getBaseUrl() : string { return this.baseUrl + this.matchId; }


  public openSquare(id: string) : Observable<Square> {
    return this.http
      .get(this.getBaseUrl() + "/open/square/" + id, this.buildDefaultOptions())
      .map(this.extractData)
      .catch(this.handleError)
      ;
  }



  public startNewMatch(): Observable<Match> {
    return this.http
      .get(this.getBaseUrl() + "/test/new/match", this.buildDefaultOptions())
      .map(this.extractData)
      .catch(this.handleError)
      ;
  }


  public submitShips(boardId: string, ships: Ship[]): Observable<boolean> {
    return this.http
      .post(this.getBaseUrl() + "/board/"+boardId+"/ships", ships , this.buildDefaultOptions())
      .map(this.extractData)
      .catch(this.handleError)
      ;
  }


  public hitMe(boardId: string): Observable<Square> {
    return this.http
      .get(this.getBaseUrl() + "/board/"+boardId+"/hitme", this.buildDefaultOptions())
      .map(this.extractData)
      .catch(this.handleError)
      ;
  }

  public getUpdatedMatch(matchId: string): Observable<Match> {
    this.matchId = matchId;
    return this.http
      .get(this.getBaseUrl() + "/poll", this.buildDefaultOptions())
      .map(this.extractData)
      .catch(this.handleError)
      ;
  }














  private buildDefaultOptions(): RequestOptions {
    let headers = new Headers({
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Methods': 'GET, POST, OPTIONS, PUT, PATCH, DELETE',
      'Access-Control-Allow-Headers': 'X-Requested-With,content-type',
      'Access-Control-Allow-Credentials': true,

    });
    let options = new RequestOptions({ headers: headers });
    return options;
}



  private extractData(res: Response) {
    let body = res.json();
    console.log(body);
    // return body.data || { };
    return body;
  }

  private handleError (error: Response | any) {

    let errMsg: string;
    if (error instanceof Response) {
      const body = error.json() || '';
      const err = body.error || JSON.stringify(body);
      errMsg = `${error.status} - ${error.statusText || ''} ${err}`;
    } else {
      errMsg = error.message ? error.message : error.toString();
    }
    console.error(errMsg);
    return Observable.throw(errMsg);
  }


}
