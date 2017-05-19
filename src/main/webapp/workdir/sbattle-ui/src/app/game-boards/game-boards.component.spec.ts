import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GameBoardsComponent } from './game-boards.component';

describe('GameBoardsComponent', () => {
  let component: GameBoardsComponent;
  let fixture: ComponentFixture<GameBoardsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GameBoardsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GameBoardsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
