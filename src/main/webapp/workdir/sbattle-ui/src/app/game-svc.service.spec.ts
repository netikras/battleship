import { TestBed, inject } from '@angular/core/testing';

import { GameSvcService } from './game-svc.service';

describe('GameSvcService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [GameSvcService]
    });
  });

  it('should be created', inject([GameSvcService], (service: GameSvcService) => {
    expect(service).toBeTruthy();
  }));
});
