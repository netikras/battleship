package com.ai.game.sbattle.service;

import com.ai.game.sbattle.data.dao.GameDao;
import com.ai.game.sbattle.data.model.GameBoard;
import com.ai.game.sbattle.data.model.Ship;
import com.ai.game.sbattle.data.model.Square;
import com.ai.game.sbattle.utils.GameBoardUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by netikras on 17.5.16.
 */
@Service
public class ComputerPlayerService {

    @Resource
    private GameDao dao;

    private final Random random = new Random();

    public void fillBoard(GameBoard board, int difficulty) {

    }

    public void hit(GameBoard board, int difficulty) {

        List<Square> remainingSquares = new ArrayList<>();
        for (Square square : board.getSquares()) {
            if (square.isRevealed()) continue;
            remainingSquares.add(square);
        }

        String[] coords = new String[remainingSquares.size()];

        for (int i = 0; i < remainingSquares.size(); i++) {
            coords[i] = remainingSquares.get(i).getCoordinates().getId();
        }

        /*
        select paper, electronic, count(*) as occurrences from recipient group by paper, electronic order by occurrences desc;
        select
            c.id,
            count(*) total_count
        from
            square sq
            join coordinates c
                on c.id = sq.coord_id
            join board b
                on game_board.id = sq.board_id
            join player p
                on p.id = b.player_id
                and p.robot <> 1
        where
            sq.hosted_ship_id is not null
            and c.id in [coords]
        group by c.id
        order by total_count desc
        ;
         */

        Square squareToHit = null;

        List<Square> squares = getRevealedSquaresWithShipsHavingMultipleHits(board);
        Square otherSquare;
        Square hittableSquare;

        if (squares != null && squares.size() > 0) {

            for (Square square : squares) {


                otherSquare = GameBoardUtils.getSquareLeftTo(board, square); // peek left -- maybe there is a contiguous block revealed
                if (otherSquare != null && otherSquare.isRevealed() && otherSquare.getHostedShip() != null) {
                    hittableSquare = GameBoardUtils.getSquareRightTo(board, square); // try 1 position right from the 'square'
                    if (hittableSquare != null && !hittableSquare.isRevealed()) {
                        hitSquare(board, hittableSquare);
                        return;
                    }
                    hittableSquare = GameBoardUtils.getSquareLeftTo(board, otherSquare); // try 2 positions left from the 'square'
                    if (hittableSquare != null && !hittableSquare.isRevealed()) {
                        hitSquare(board, hittableSquare);
                        return;
                    }
                }


                otherSquare = GameBoardUtils.getSquareRightTo(board, square); // peek right -- maybe there is a contiguous block revealed
                if (otherSquare != null && otherSquare.isRevealed() && otherSquare.getHostedShip() != null) {
                    hittableSquare = GameBoardUtils.getSquareLeftTo(board, square); // try 1 position left from the 'square'
                    if (hittableSquare != null && !hittableSquare.isRevealed()) {
                        hitSquare(board, hittableSquare);
                        return;
                    }
                    hittableSquare = GameBoardUtils.getSquareRightTo(board, otherSquare); // try 2 positions right from the 'square'
                    if (hittableSquare != null && !hittableSquare.isRevealed()) {
                        hitSquare(board, hittableSquare);
                        return;
                    }
                }

                otherSquare = GameBoardUtils.getSquareAbove(board, square); // peek above -- maybe there is a contiguous block revealed
                if (otherSquare != null && otherSquare.isRevealed() && otherSquare.getHostedShip() != null) {
                    hittableSquare = GameBoardUtils.getSquareBelow(board, square); // try 1 position below from the 'square'
                    if (hittableSquare != null && !hittableSquare.isRevealed()) {
                        hitSquare(board, hittableSquare);
                        return;
                    }
                    hittableSquare = GameBoardUtils.getSquareAbove(board, otherSquare); // try 2 positions above from the 'square'
                    if (hittableSquare != null && !hittableSquare.isRevealed()) {
                        hitSquare(board, hittableSquare);
                        return;
                    }
                }

                otherSquare = GameBoardUtils.getSquareBelow(board, square); // peek below -- maybe there is a contiguous block revealed
                if (otherSquare != null && otherSquare.isRevealed() && otherSquare.getHostedShip() != null) {
                    hittableSquare = GameBoardUtils.getSquareAbove(board, square); // try 1 position above from the 'square'
                    if (hittableSquare != null && !hittableSquare.isRevealed()) {
                        hitSquare(board, hittableSquare);
                        return;
                    }
                    hittableSquare = GameBoardUtils.getSquareBelow(board, otherSquare); // try 2 positions below from the 'square'
                    if (hittableSquare != null && !hittableSquare.isRevealed()) {
                        hitSquare(board, hittableSquare);
                        return;
                    }
                }

            }


            throw new RuntimeException("Computer has no idea where to hit. Spot number: 1");
        }

        squares = getRevealedSquaresWithShipsHavingSingleHit(board);
        if (squares != null && squares.size() > 0) {
            for (Square square : squares) {
                otherSquare = GameBoardUtils.getSquareLeftTo(board, square);
                if (otherSquare != null && !otherSquare.isRevealed()) {
                    hitSquare(board, otherSquare);
                    return;
                }
                otherSquare = GameBoardUtils.getSquareRightTo(board, square);
                if (otherSquare != null && !otherSquare.isRevealed()) {
                    hitSquare(board, otherSquare);
                    return;
                }
                otherSquare = GameBoardUtils.getSquareAbove(board, square);
                if (otherSquare != null && !otherSquare.isRevealed()) {
                    hitSquare(board, otherSquare);
                    return;
                }
                otherSquare = GameBoardUtils.getSquareBelow(board, square);
                if (otherSquare != null && !otherSquare.isRevealed()) {
                    hitSquare(board, otherSquare);
                    return;
                }

            }
            throw new RuntimeException("Computer has no idea where to hit. Spot number: 2");
        }

        squareToHit = getBestGuessedOccupiedSquare(board);

        if (squareToHit != null) { // very likely to be 0 at the beginning of data collection to DB
            hitSquare(board, squareToHit);
            return;
        }

        squareToHit = getRandomlyGuessedOccupiedSquare(board);

        hitSquare(board, squareToHit);
    }


    private void hitSquare(GameBoard board, Square square) {
        square.setRevealed(true);
        dao.update(square);

        Ship ship = square.getHostedShip();

        if (ship != null) {
            boolean killed = true;
            List<Square> shipSquares = ship.getSquares();
            for (Square shipSquare : shipSquares) {
                if (!shipSquare.isRevealed()) {
                    killed = false;
                    break;
                }
            }

            if (killed) {
                ship.setKilled(killed);
                dao.update(ship);
            }
        }
    }


    private List<Square> getRevealedSquaresWithShipsHavingMultipleHits(GameBoard board) {
        List<Square> squares = new ArrayList<>();

        for (Ship ship : getNotKilledShips(board)) {
            Square previouslySeenHit = null;
            boolean seen = false;
            for (Square square : ship.getSquares()) {
                if (square.isRevealed()) {
                    if (previouslySeenHit != null) {
                        squares.add(square);
                        if (!seen) { // to not to add the first seen square twice or more
                            squares.add(previouslySeenHit);
                        }
                        seen = true;
                    } else {
                        previouslySeenHit = square;
                    }
                }
            }
        }

        return squares;
    }


    private List<Square> getRevealedSquaresWithShipsHavingSingleHit(GameBoard board) {
        List<Square> squares = new ArrayList<>();

        for (Ship ship : getNotKilledShips(board)) {
            Square previouslySeenHit = null;
            boolean seenMultipleHits = false;
            for (Square square : ship.getSquares()) {
                if (square.isRevealed()) {
                    if (previouslySeenHit == null) {
                        previouslySeenHit = square;
                    } else {
                        seenMultipleHits = true;
                        break;
                    }
                }
            }

            if (seenMultipleHits) {
                continue;
            }
            if (previouslySeenHit != null) {
                squares.add(previouslySeenHit);
            }
        }

        return squares;
    }

    private Square getBestGuessedOccupiedSquare(GameBoard board) {
        List<Square> hittableSquares = dao.getSquaresToHit(getCoveredSquares(board));
        if (hittableSquares == null || hittableSquares.size() == 0) {
            return null;
        }
        return hittableSquares.get(0); // the further from the beginning - the less chances are to hit a ship. A good place for difficulty leveling at offence
    }

    private Square getRandomlyGuessedOccupiedSquare(GameBoard board) {
        List<Square> remainingSquares = getCoveredSquares(board);

        return remainingSquares.get(random.nextInt(remainingSquares.size()));
    }

    private List<Ship> getNotKilledShips(GameBoard board) {
        List<Ship> ships = new ArrayList<>();

        for (Ship ship :board.getShips()) {
            for (Square square : ship.getSquares()) {
                if (!square.isRevealed()) {
                    ships.add(ship);
                    break;
                }
            }
        }

        return ships;
    }

    private List<Square> getCoveredSquares(GameBoard board) {
        List<Square> squares = new ArrayList<>();

        for (Square square : board.getSquares()) {
            if (square.isRevealed()) continue;
            squares.add(square);
        }

        return squares;
    }


}
