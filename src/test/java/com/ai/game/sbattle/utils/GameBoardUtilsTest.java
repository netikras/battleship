package com.ai.game.sbattle.utils;

import com.ai.game.sbattle.data.model.GameBoard;
import com.ai.game.sbattle.data.model.GameMatch;
import com.ai.game.sbattle.data.model.Square;
import com.ai.game.sbattle.service.GameService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by netikras on 17.5.16.
 */
public class GameBoardUtilsTest {

    private GameService gameService;

    @Before
    public void init() {
        gameService = new GameService();
    }

    @Test
    public void getLeftTest() {
        GameMatch match = gameService.createNewMatch("Test Match");
        GameBoard board = match.getPlayerB().getBoard();

        Square square = board.getSquares().get(24);
        Square otherSquare = GameBoardUtils.getSquareLeftTo(board, square);
        System.out.println(square.getCoordinates());
        System.out.println(otherSquare.getCoordinates());

        Assert.assertEquals("New square X must be lower by 1 than first square's", square.getCoordinates().getX() - 1, otherSquare.getCoordinates().getX());
        Assert.assertEquals("New square Y must not have changed", square.getCoordinates().getY(), otherSquare.getCoordinates().getY());

        Assert.assertTrue("New square must be on the left from the original one", GameBoardUtils.isOnLeft(board, otherSquare, square));
        Assert.assertFalse("Original square must not be on the left of the new one", GameBoardUtils.isOnLeft(board, square, otherSquare));
        Assert.assertFalse("New square must not be on the right of the original one", GameBoardUtils.isOnRight(board, otherSquare, square));
        Assert.assertFalse("New square must not be above the original one", GameBoardUtils.isAbove(board, otherSquare, square));
        Assert.assertFalse("New square must not be below the original one", GameBoardUtils.isBelow(board, otherSquare, square));

    }

    @Test
    public void getRightTest() {
        GameMatch match = gameService.createNewMatch("Test Match");
        GameBoard board = match.getPlayerB().getBoard();

        Square square = board.getSquares().get(24);
        Square otherSquare = GameBoardUtils.getSquareRightTo(board, square);
        System.out.println(square.getCoordinates());
        System.out.println(otherSquare.getCoordinates());

        Assert.assertEquals("New square X must be higher by 1 than first square's", square.getCoordinates().getX() + 1, otherSquare.getCoordinates().getX());
        Assert.assertEquals("New square Y must not have changed", square.getCoordinates().getY(), otherSquare.getCoordinates().getY());

        Assert.assertTrue("New square must be on the right from the original one", GameBoardUtils.isOnRight(board, otherSquare, square));
        Assert.assertFalse("Original square must not be on the right of the new one", GameBoardUtils.isOnRight(board, square, otherSquare));
        Assert.assertFalse("New square must not be on the left of the original one", GameBoardUtils.isOnLeft(board, otherSquare, square));
        Assert.assertFalse("New square must not be above the original one", GameBoardUtils.isAbove(board, otherSquare, square));
        Assert.assertFalse("New square must not be below the original one", GameBoardUtils.isBelow(board, otherSquare, square));

    }

    @Test
    public void getAboveTest() {
        GameMatch match = gameService.createNewMatch("Test Match");
        GameBoard board = match.getPlayerB().getBoard();

        Square square = board.getSquares().get(24);
        Square otherSquare = GameBoardUtils.getSquareAbove(board, square);
        System.out.println(square.getCoordinates());
        System.out.println(otherSquare.getCoordinates());

        Assert.assertEquals("New square X must not have changed", square.getCoordinates().getX(), otherSquare.getCoordinates().getX());
        Assert.assertEquals("New square Y must be lower by 1 than first square's", square.getCoordinates().getY() - 1, otherSquare.getCoordinates().getY());

        Assert.assertTrue("New square must be above the original one", GameBoardUtils.isAbove(board, otherSquare, square));
        Assert.assertFalse("Original square must not be above the new one", GameBoardUtils.isAbove(board, square, otherSquare));
        Assert.assertFalse("New square must not be on the left of the original one", GameBoardUtils.isOnLeft(board, otherSquare, square));
        Assert.assertFalse("New square must not be on the right of the original one", GameBoardUtils.isOnRight(board, otherSquare, square));

    }

    @Test
    public void getBelowTest() {
        GameMatch match = gameService.createNewMatch("Test Match");
        GameBoard board = match.getPlayerB().getBoard();

        Square square = board.getSquares().get(24);
        Square otherSquare = GameBoardUtils.getSquareBelow(board, square);
        System.out.println(square.getCoordinates());
        System.out.println(otherSquare.getCoordinates());

        Assert.assertEquals("New square X must not have changed", square.getCoordinates().getX(), otherSquare.getCoordinates().getX());
        Assert.assertEquals("New square Y must be higher by 1 than first square's", square.getCoordinates().getY() + 1, otherSquare.getCoordinates().getY());

        Assert.assertTrue("New square must be below the original one", GameBoardUtils.isBelow(board, otherSquare, square));
        Assert.assertFalse("Original square must not be below the new one", GameBoardUtils.isBelow(board, square, otherSquare));
        Assert.assertFalse("New square must not be on the left of the original one", GameBoardUtils.isOnLeft(board, otherSquare, square));
        Assert.assertFalse("New square must not be on the right of the original one", GameBoardUtils.isOnRight(board, otherSquare, square));

    }

}
