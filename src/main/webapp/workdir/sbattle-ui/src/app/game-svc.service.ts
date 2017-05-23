import { Injectable }              from '@angular/core';
import { Http, Response }          from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';

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

  private matchId: string = "";

  private getBaseUrl(mId: string) : string { return this.baseUrl + this.matchId; }


  openSquare(id: string) : Observable<Square> {
    return this.http
      .get("")
      .map(this.extractData)
      .catch(this.handleError)
      ;
  }


  private extractData(res: Response) {
    let body = res.json();
    return body.data || { };
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
