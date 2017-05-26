package com.ai.game.sbattle.service;

import com.ai.game.sbattle.data.dao.GameDao;
import com.ai.game.sbattle.data.model.GameBoard;
import com.ai.game.sbattle.data.model.GameMatch;
import com.ai.game.sbattle.data.model.Ship;
import com.ai.game.sbattle.utils.ConsoleVisualizer;
import com.ai.game.sbattle.utils.GameBoardUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by netikras on 17.5.16.
 */
@RunWith(MockitoJUnitRunner.class)
public class ComputerPlayerServiceTest {

    @Mock
    private GameDao dao;

    @InjectMocks
    private GameService gameService;

    @InjectMocks
    private ComputerPlayerService computerPlayerService;


    @Before
    public void init() {
//        computerPlayerService = new ComputerPlayerService();
//        gameService = new GameService();
    }

    @Test
    public void hitTest() {


        GameMatch match = gameService.createNewMatch("TestGame");

        GameBoardUtils.fillWithShipsRandomly(match.getPlayerA().getBoard(), true);
        GameBoardUtils.fillWithShipsRandomly(match.getPlayerB().getBoard(), true);

        GameBoard board = match.getPlayerA().getBoard();
        Ship ship = board.getShips().get(0);
        ship.getSquares().get(0).setRevealed(true);
        ship.getSquares().get(1).setRevealed(true);

        computerPlayerService.hit(match.getPlayerA().getBoard(), 0);

        System.out.println();

        ConsoleVisualizer.drawBoard(match.getPlayerA().getBoard());
    }



    @Test
    public void prepareBoard() {
        GameMatch match = gameService.createNewMatch("testGame");
        computerPlayerService.fillBoard(match.getPlayerA().getBoard(), 0);

        ConsoleVisualizer.drawBoard(match.getPlayerA().getBoard());

    }
}
