import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GameTooltipsComponent } from './game-tooltips.component';

describe('GameTooltipsComponent', () => {
  let component: GameTooltipsComponent;
  let fixture: ComponentFixture<GameTooltipsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GameTooltipsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GameTooltipsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
