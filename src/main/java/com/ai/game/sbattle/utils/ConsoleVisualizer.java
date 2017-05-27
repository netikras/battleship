package com.ai.game.sbattle.utils;

import com.ai.game.sbattle.data.model.GameBoard;
import com.ai.game.sbattle.data.model.Square;

/**
 * Created by netikras on 17.5.17.
 */
public class ConsoleVisualizer {


    public static final char SHIP = 'O';
    public static final char SHIP_HIT = 'Ø';
    public static final char HIDDEN = '#';
    public static final char EMPTY = '~';


    public static void drawBoard(GameBoard board) {

        int width = (int) Math.sqrt(board.getSquares().size());

        char[][] matrix = new char[width][width*2];

        for (Square square : board.getSquares()) {

            char block;

            if (square.getHostedShip() == null) {
                block = EMPTY;
            } else {
                block = SHIP;
                block = (char) ((char)square.getHostedShip().getType().ordinal() + '0');
            }

            matrix
                    [square.getCoordinates().getX()]
                    [square.getCoordinates().getY()*2]
                = block;
            matrix
                    [square.getCoordinates().getX()]
                    [square.getCoordinates().getY()*2+1]
                    = ' ';
        }


        System.out.println("Player: " + board.getBoardOwner().getId());
        System.out.println("Board:  " + board.getId());

        for (char[] row : matrix) {
            System.out.println(new String(row));
        }
    }


    public static void drawOpponentBoard(GameBoard board) {

        int width = (int) Math.sqrt(board.getSquares().size());

        char[][] matrix = new char[width][width*2];

        for (Square square : board.getSquares()) {

            char block;

            if (square.isRevealed()) {
                if (square.getHostedShip() != null) {
                    block = SHIP_HIT;
                } else {
                    block = EMPTY;
                }
            } else {
                block = HIDDEN;
            }

            matrix
                    [square.getCoordinates().getX()]
                    [square.getCoordinates().getY()*2]
                    = block;
            matrix
                    [square.getCoordinates().getX()]
                    [square.getCoordinates().getY()*2+1]
                    = ' ';
        }


        System.out.println("Player: " + board.getBoardOwner().getId());
        System.out.println("Board:  " + board.getId());

        for (char[] row : matrix) {
            System.out.println(new String(row));
        }
    }

}
