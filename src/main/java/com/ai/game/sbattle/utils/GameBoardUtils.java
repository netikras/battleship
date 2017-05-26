package com.ai.game.sbattle.utils;

import com.ai.game.sbattle.data.model.Coordinates;
import com.ai.game.sbattle.data.model.GameBoard;
import com.ai.game.sbattle.data.model.Ship;
import com.ai.game.sbattle.data.model.Square;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by netikras on 17.5.16.
 */
public class GameBoardUtils {


    private static final Random random = new Random();

    public static Square getSquareLeftTo(GameBoard board, Square obj) {
        Coordinates coordinates = obj.getCoordinates();

        if (coordinates.getX() == 0) {
            // this square is the most left one
            return null;
        }

        for (Square square : board.getSquares()) {
            if (square.getCoordinates().getY() == coordinates.getY()
                    && square.getCoordinates().getX() == coordinates.getX() - 1) {
                return square;
            }
        }
        throw new RuntimeException("Cannot find a square positioned to left from " + obj + ". Probably game board has mistakes");
    }

    public static Square getSquareRightTo(GameBoard board, Square obj) {
        Coordinates coordinates = obj.getCoordinates();

        int width = (int) Math.sqrt(board.getSquares().size());

        if (coordinates.getX() == width - 1) {
            // this square is the most right one
            return null;
        }

        for (Square square : board.getSquares()) {
            if (square.getCoordinates().getY() == coordinates.getY()
                    && square.getCoordinates().getX() == coordinates.getX() + 1) {
                return square;
            }
        }
        throw new RuntimeException("Cannot find a square positioned to right from " + obj + ". Probably game board has mistakes");
    }

    public static Square getSquareAbove(GameBoard board, Square obj) {
        Coordinates coordinates = obj.getCoordinates();

        if (coordinates.getY() == 0) {
            // this square is the most left one
            return null;
        }

        for (Square square : board.getSquares()) {
            if (square.getCoordinates().getX() == coordinates.getX()
                    && square.getCoordinates().getY() == coordinates.getY() - 1) {
                return square;
            }
        }
        throw new RuntimeException("Cannot find a square positioned to left from " + obj + ". Probably game board has mistakes");
    }

    public static Square getSquareBelow(GameBoard board, Square obj) {
        Coordinates coordinates = obj.getCoordinates();

        int width = (int) Math.sqrt(board.getSquares().size());

        if (coordinates.getY() == width - 1) {
            // this square is the most right one
            return null;
        }

        for (Square square : board.getSquares()) {
            if (square.getCoordinates().getX() == coordinates.getX()
                    && square.getCoordinates().getY() == coordinates.getY() + 1) {
                return square;
            }
        }
        throw new RuntimeException("Cannot find a square positioned to right from " + obj + ". Probably game board has mistakes");
    }


    public static boolean isOnLeft(GameBoard board, Square what, Square fromWhat) {
        return fromWhat.getCoordinates().getX() > 0
                && what.getCoordinates().getX() < fromWhat.getCoordinates().getX()
                ;
    }

    public static boolean isOnRight(GameBoard board, Square what, Square fromWhat) {
        int width = (int) Math.sqrt(board.getSquares().size());
        return fromWhat.getCoordinates().getX() < width - 1
                && what.getCoordinates().getX() > fromWhat.getCoordinates().getX()
                ;
    }

    public static boolean isAbove(GameBoard board, Square what, Square fromWhat) {
        return fromWhat.getCoordinates().getY() > 0
                && what.getCoordinates().getY() < fromWhat.getCoordinates().getY()
                ;
    }

    public static boolean isBelow(GameBoard board, Square what, Square fromWhat) {
        int width = (int) Math.sqrt(board.getSquares().size());
        return fromWhat.getCoordinates().getY() < width - 1
                && what.getCoordinates().getY() > fromWhat.getCoordinates().getY()
                ;
    }


    public static void fillWithShipsRandomly(GameBoard board, boolean setIDs) {
        if (board.getShips() != null && board.getShips().size() > 0) {
            throw new IllegalStateException("Game board already has " + board.getShips().size() + " ships. Cannot fill it randomly");
        }

        if (board.getSquares() == null || board.getSquares().size() == 0) {
            throw new IllegalStateException("Game board has no squares. Cannot fill it with boats - there's no water to swim in");
        }

        if (board.getShips() == null) {
            board.setShips(new ArrayList<>());
        }

        for (Ship.ShipType shipType : Ship.ShipType.values()) {
            int shipSize = shipType.getShipSize();
            int shipsCount = shipType.getCount();

            for (int i = 0; i < shipsCount; i++) {
                List<Square> shipSquares = getNextPristineBlock(board, shipSize);
                Ship ship = new Ship();
                if (setIDs)
                    ship.setId(UUID.randomUUID().toString());
                ship.setKilled(false);
                ship.setBoard(board);
                ship.setType(shipType);
                ship.setSquares(shipSquares);

                for (Square square : shipSquares) {
                    square.setHostedShip(ship);
                }

                board.getShips().add(ship);
            }
        }

    }

    public static Square getSquareInCoordinate(List<Square> squares, String coordId) {
        for (Square square : squares) {
            if (square.getCoordinates().getId().equals(coordId)) {
                return square;
            }
        }

        return null;
    }

    public static Square getSquare(GameBoard board, int x, int y) {
        return getSquare(board.getSquares(), x, y);
    }

    public static Square getSquare(List<Square> squares, int x, int y) {
        for (Square square : squares) {
            if (square.getCoordinates().getX() == x && square.getCoordinates().getY() == y) {
                return square;
            }
        }

        return null;
    }

    public static List<Square> getNextPristineBlock(GameBoard board, int size) {
        List<Square> squares = new ArrayList<>();

        int width = (int) Math.sqrt(board.getSquares().size());

        while (true) {
            boolean vertical = random.nextBoolean();
            int firstSquareX = random.nextInt(width);
            int firstSquareY = random.nextInt(width);

            squares.clear();


            Square firstSquare = getSquare(board, firstSquareX, firstSquareY);
            Square nextSquare;
            int missingSquares = size;

            if (firstSquare == null) {
                continue;
            }

            if (firstSquare.getHostedShip() != null) {
                continue; // try again
            }

            missingSquares--;

            squares.add(firstSquare);

            if (vertical) {
                nextSquare = getSquareAbove(board, firstSquare);
                while (missingSquares > 0) {
                    if (nextSquare != null && nextSquare.getHostedShip() == null) {
                        squares.add(nextSquare);
                        nextSquare = getSquareAbove(board, nextSquare);
                        missingSquares--;
                    } else {
                        break;
                    }
                }

                nextSquare = getSquareBelow(board, firstSquare);

                while (missingSquares > 0) {
                    if (nextSquare != null && nextSquare.getHostedShip() == null) {
                        squares.add(nextSquare);
                        nextSquare = getSquareBelow(board, nextSquare);
                        missingSquares--;
                    } else {
                        break;
                    }
                }


            } else {
                nextSquare = getSquareLeftTo(board, firstSquare);
                while (missingSquares > 0) {
                    if (nextSquare != null && nextSquare.getHostedShip() == null) {
                        squares.add(nextSquare);
                        nextSquare = getSquareLeftTo(board, nextSquare);
                        missingSquares--;
                    } else {
                        break;
                    }
                }

                nextSquare = getSquareRightTo(board, firstSquare);

                while (missingSquares > 0) {
                    if (nextSquare != null && nextSquare.getHostedShip() == null) {
                        squares.add(nextSquare);
                        nextSquare = getSquareRightTo(board, nextSquare);
                        missingSquares--;
                    } else {
                        break;
                    }
                }
            }

            if (missingSquares == 0) {
                break;
            }

        }

        return squares;
    }


    public static List<Square> getNewRandomBoardSquares() {
        List<Square> squares = new ArrayList<>();


        return squares;
    }


    public static Square getSquare(GameBoard board, String id) {
        for (Square square : board.getSquares()) {
            if (id.equals(square.getId())) {
                return square;
            }
        }
        return null;
    }
}
