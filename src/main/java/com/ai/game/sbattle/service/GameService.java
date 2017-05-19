package com.ai.game.sbattle.service;

import com.ai.game.sbattle.data.dao.GameDao;
import com.ai.game.sbattle.data.model.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by netikras on 17.5.15.
 */
@Service

public class GameService {

    @Resource
    private GameDao dao;

    public void hit(String matchId, String squareId) {
        Square square = dao.getSquareById(squareId);
        GameBoard board = square.getBoard();

        if (square.isRevealed()) {
            throw new IllegalStateException("Square is already revealed");
        }

        Ship ship = square.getHostedShip();
        square.setRevealed(true);

        dao.update(square);

        if (ship == null) {
            // Missed
            return;
        }


        int revealedShipSquares = 0;

        for (Square shipSquares : ship.getSquares()) {
            if (shipSquares.isRevealed()) {
                revealedShipSquares++;
            }
        }

        if (revealedShipSquares >= ship.getType().getShipSize()) {
            ship.setKilled(true);
        }

        dao.update(ship);

    }

    @SuppressWarnings("Duplicates")
    public boolean validateBoardShips(GameBoard board, int shipsCount) {

        List<Ship> ships = board.getShips();
        List<Square> squares;
        Map<String, Object> squaresSet = new HashMap<>();
        Map<Ship.ShipType, Integer> shipsByType = new HashMap<>();
        int boardWidth = (int) Math.sqrt(board.getSquares().size());

        if (ships.size() != shipsCount) {
            return false;
        }

        for (Ship ship : ships) {
            squares = ship.getSquares();
            if (ship.getType().getShipSize() != squares.size()) {
                // Ship is too long
                return false;
            }

            Integer shipTypeCount = shipsByType.get(ship.getType());
            if (shipTypeCount == null) {
                shipTypeCount = new Integer(0);
            }
            if (shipTypeCount.intValue() >= ship.getType().getCount()) {
                // too many ships of that kind
                return false;
            }
            shipsByType.put(ship.getType(), new Integer(shipTypeCount.intValue() + 1));

            int[] pos_x = new int[squares.size()];
            int[] pos_y = new int[squares.size()];

            pos_x[0] = -1;

            for (int i = 0; i < squares.size(); i++) {
                Square square = squares.get(i);

                if (squaresSet.put(square.getId(), ship) != null) {
                    // ship square overlaps with another ship
                    return false;
                }

                if (square.getCoordinates().getX() >= boardWidth || square.getCoordinates().getY() >= boardWidth) {
                    // square is out of board bounds
                    return false;
                }
                if (square.getCoordinates().getX() < 0 || square.getCoordinates().getY() < 0) {
                    // square is out of board bounds
                    return false;
                }

                pos_x[i] = square.getCoordinates().getX();
                pos_y[i] = square.getCoordinates().getY();

            }

            if (pos_x.length > 1) { // if there's more than one square per that ship
                // ascending
                Arrays.sort(pos_x);
                Arrays.sort(pos_y);

                if (pos_x[1] == pos_x[0]) {
                    // Ship is vertical
                    for (int i = 0; i < pos_x.length; i++) {
                        if (pos_x[i] != pos_x[0]) {
                            // X coordinates must be equal. Ship cannot just bend..
                            return false;
                        }

                        if (i > 1) {
                            if (pos_y[i] - pos_y[i - 1] != 1) {
                                // There is a hole in a ship. Design flaw?
                                // i.e. ship vertical IDs are not consequent
                                return false;
                            }
                        }
                    }
                } else {
                    // Ship is horizontal
                    for (int i = 0; i < pos_x.length; i++) {
                        if (pos_y[i] != pos_y[0]) {
                            // Y coordinates must be equal. Ship cannot just bend..
                            return false;
                        }

                        if (i > 1) {
                            if (pos_x[i] - pos_x[i - 1] != 1) {
                                // There is a hole in a ship. Design flaw?
                                // i.e. ship vertical IDs are not consequent
                                return false;
                            }
                        }
                    }
                }
            }

        }

        return true;
    }


    public Player createNewPlayer(String id) {
        Player player = new Player();
        player.setId(id);
        return player;
    }

    public GameBoard createNewBoard(Player player, int width) {
        GameBoard board = new GameBoard();
        board.setId(UUID.randomUUID().toString());
        board.setBoardOwner(player);

        int squaresCount = (int) Math.pow(width, 2);

        List<Square> squares = new ArrayList<>(squaresCount);
        int pos_x = 0;
        int pos_y = 0;

        for (int i = 0; i < squaresCount; i++) {

            Square square = new Square();
            square.setId(UUID.randomUUID().toString());
            if (pos_x == width) {
                pos_x = 0;
                pos_y++;
            }
            Coordinates coordinates = new Coordinates(pos_x++, pos_y);
            coordinates.setRatio(i);
            square.setCoordinates(coordinates);
            square.setRevealed(false);
            square.setBoard(board);

            squares.add(square);
        }

        board.setSquares(squares);

        player.setBoard(board);

        return board;
    }


    public GameMatch createNewMatch(String gameId) {

        GameMatch match = new GameMatch();

        match.setId(gameId);
        match.setPlayerA(createNewPlayer(UUID.randomUUID().toString()));
        match.setPlayerB(createNewPlayer(UUID.randomUUID().toString()));
        createNewBoard(match.getPlayerA(), 10);
        createNewBoard(match.getPlayerB(), 10);

        return match;
    }



    public GameMatch getMatch(String gameId) {
        System.out.println("Getting match [service]: " + gameId);
        return dao.getMatchById(gameId);
    }
}
