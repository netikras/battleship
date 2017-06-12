package com.ai.game.sbattle.service;

import com.ai.game.sbattle.data.dao.GameDao;
import com.ai.game.sbattle.data.model.Coordinates;
import com.ai.game.sbattle.data.model.GameBoard;
import com.ai.game.sbattle.data.model.Ship;
import com.ai.game.sbattle.data.model.Square;
import com.ai.game.sbattle.utils.GameBoardUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by netikras on 17.5.16.
 */
@Service
public class ComputerPlayerService {

    @Resource
    private GameDao dao;

    private final Random random = new Random();

    public void fillBoard(GameBoard board, int difficulty) {
    /*
    1. rasti langelius, kuriuos žaidėjas šaudo rečiausiai
    2. tuose langeliuose apgyvendinti mažiausius laivus (1 langelio dydžio)

    3. suindeksuoti visus langelius pagal jų pažeidžiamumą unikaliais skaičiais (select.*order by .* desc)
    4. ieškoti laisvų (nepastatytų) langelių blokų (kiekvieno laivo dydžio), kurių langelių koeficientų suma būtų mažiausia.
    5. tuos langelius apstatyti laivais.

    6. Laikytis principo: kuo mažesnė tikimybė, kad laivas bus pamuštas - tuo mažesnį laivą ten statyti
     */

        Ship.ShipType[] shipTypes = Ship.ShipType.values();
        if (board.getShips() == null) board.setShips(new ArrayList<>(20));

        for (int i = shipTypes.length - 1; i >= 0; i--) { // iterating backwards so that we'd start from the smallest ship
            Ship.ShipType shipType = shipTypes[i];

            for (int c = 0; c < shipType.getCount(); c++) {

                List<Square> squaresBlock = getBestSuitedSquaresForShip(board, shipType);

                if (squaresBlock != null && squaresBlock.size() > 0) {
                    Ship ship = new Ship();
                    ship.setBoard(board);
                    ship.setKilled(false);
//                    ship.setId(UUID.randomUUID().toString());
                    ship.setType(shipType);

                    for (Square square : squaresBlock) {
                        square.setHostedShip(ship);
                    }
                    ship.setSquares(squaresBlock);
                    board.getShips().add(ship);
                }

            }
        }

        if (board.getShips() == null || board.getShips().size() == 0) {
            GameBoardUtils.fillWithShipsRandomly(board, false);
        }


    }

    private List<Square> getBestSuitedSquaresForShip(GameBoard board, Ship.ShipType shipType) {

        int currentBestRatio = Integer.MAX_VALUE;
        int neededSquaresCount = shipType.getShipSize();
        int calculatedRatio;

        List<Square> chosenSquares = new ArrayList<>(neededSquaresCount);
        List<Square> workspaceList = new ArrayList<>(neededSquaresCount);
        List temp;

        List<Square> sortedSquaresByRatioDesc = getSquaresForFillingBoardSortedDesc(board);
        Square possibleBlockCandidate;
        boolean blockLookupTerminated = false;


        for (Square firstSquare : sortedSquaresByRatioDesc) {

            if (!canSquareBeUsedForShipBlock(firstSquare)) {
                continue;
            }
            blockLookupTerminated = false;
            workspaceList.clear();
            workspaceList.add(firstSquare);
            possibleBlockCandidate = firstSquare;
            for (int i = 1; i < neededSquaresCount; i++) {
                possibleBlockCandidate = GameBoardUtils.getSquareRightTo(board, possibleBlockCandidate);

                if (canSquareBeUsedForShipBlock(possibleBlockCandidate)) {
                    workspaceList.add(possibleBlockCandidate);
                } else {
                    blockLookupTerminated = true;
                    break;
                }
            }

            if (!blockLookupTerminated) {
                calculatedRatio = calculateSquareBlockRatio(workspaceList);
                // Of course I could calculate ratio on-the-go and terminate block lookup sooner.
                // But then again there are only a very few ships so I do not give a damn

                if (calculatedRatio < currentBestRatio) {
                    currentBestRatio = calculatedRatio;

                    // reusing objects like a boss :D
                    temp = chosenSquares;
                    chosenSquares = workspaceList;
                    workspaceList = temp;
                }
            }

            blockLookupTerminated = false;
            workspaceList.clear();
            workspaceList.add(firstSquare);
            possibleBlockCandidate = firstSquare;
            for (int i = 0; i < neededSquaresCount; i++) {
                possibleBlockCandidate = GameBoardUtils.getSquareBelow(board, possibleBlockCandidate);

                if (canSquareBeUsedForShipBlock(possibleBlockCandidate)) {
                    workspaceList.add(possibleBlockCandidate);
                } else {
                    blockLookupTerminated = true;
                    break;
                }
            }

            if (!blockLookupTerminated) {
                calculatedRatio = calculateSquareBlockRatio(workspaceList);

                if (calculatedRatio < currentBestRatio) {
                    currentBestRatio = calculatedRatio;

                    // reusing objects like a boss :D
                    temp = chosenSquares;
                    chosenSquares = workspaceList;
                    workspaceList = temp;
                }
            }

        }


        return chosenSquares;
    }

    private int calculateSquareBlockRatio(List<Square> workspaceList) {
        int ratio = 0;

        for (Square square : workspaceList) {
            ratio += square.getCoordinates().getRatio();
        }

        return ratio;
    }


    private boolean canSquareBeUsedForShipBlock(Square square) {
        return square != null
                && square.getHostedShip() == null
                ;
    }

    private List<Square> getSquaresForFillingBoardSortedDesc(GameBoard board) {

        List<Coordinates> coordinates = dao.getCoordinatesSortedByHitCountAsc(board.getId());

        List<Square> squares = new ArrayList<>();
        int ratio = 0;

        for (Coordinates coord : coordinates) {
            coord.setRatio(ratio++);
            squares.add(GameBoardUtils.getSquare(board, coord.getX(), coord.getY()));
        }

        return squares;
    }


    public Square hit(GameBoard board, int difficulty) {

        List<Square> remainingSquares = new ArrayList<>();
        for (Square square : board.getSquares()) {
            if (square.isRevealed()) continue;
            remainingSquares.add(square);
        }

        String[] coords = new String[remainingSquares.size()];

        for (int i = 0; i < remainingSquares.size(); i++) {
            coords[i] = remainingSquares.get(i).getCoordinates().getId();
        }

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
                        return hittableSquare;
                    }
                    hittableSquare = GameBoardUtils.getSquareLeftTo(board, otherSquare); // try 2 positions left from the 'square'
                    if (hittableSquare != null && !hittableSquare.isRevealed()) {
                        hitSquare(board, hittableSquare);
                        return hittableSquare;
                    }
                }


                otherSquare = GameBoardUtils.getSquareRightTo(board, square); // peek right -- maybe there is a contiguous block revealed
                if (otherSquare != null && otherSquare.isRevealed() && otherSquare.getHostedShip() != null) {
                    hittableSquare = GameBoardUtils.getSquareLeftTo(board, square); // try 1 position left from the 'square'
                    if (hittableSquare != null && !hittableSquare.isRevealed()) {
                        hitSquare(board, hittableSquare);
                        return hittableSquare;
                    }
                    hittableSquare = GameBoardUtils.getSquareRightTo(board, otherSquare); // try 2 positions right from the 'square'
                    if (hittableSquare != null && !hittableSquare.isRevealed()) {
                        hitSquare(board, hittableSquare);
                        return hittableSquare;
                    }
                }

                otherSquare = GameBoardUtils.getSquareAbove(board, square); // peek above -- maybe there is a contiguous block revealed
                if (otherSquare != null && otherSquare.isRevealed() && otherSquare.getHostedShip() != null) {
                    hittableSquare = GameBoardUtils.getSquareBelow(board, square); // try 1 position below from the 'square'
                    if (hittableSquare != null && !hittableSquare.isRevealed()) {
                        hitSquare(board, hittableSquare);
                        return hittableSquare;
                    }
                    hittableSquare = GameBoardUtils.getSquareAbove(board, otherSquare); // try 2 positions above from the 'square'
                    if (hittableSquare != null && !hittableSquare.isRevealed()) {
                        hitSquare(board, hittableSquare);
                        return hittableSquare;
                    }
                }

                otherSquare = GameBoardUtils.getSquareBelow(board, square); // peek below -- maybe there is a contiguous block revealed
                if (otherSquare != null && otherSquare.isRevealed() && otherSquare.getHostedShip() != null) {
                    hittableSquare = GameBoardUtils.getSquareAbove(board, square); // try 1 position above from the 'square'
                    if (hittableSquare != null && !hittableSquare.isRevealed()) {
                        hitSquare(board, hittableSquare);
                        return hittableSquare;
                    }
                    hittableSquare = GameBoardUtils.getSquareBelow(board, otherSquare); // try 2 positions below from the 'square'
                    if (hittableSquare != null && !hittableSquare.isRevealed()) {
                        hitSquare(board, hittableSquare);
                        return hittableSquare;
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
                    return otherSquare;
                }
                otherSquare = GameBoardUtils.getSquareRightTo(board, square);
                if (otherSquare != null && !otherSquare.isRevealed()) {
                    hitSquare(board, otherSquare);
                    return otherSquare;
                }
                otherSquare = GameBoardUtils.getSquareAbove(board, square);
                if (otherSquare != null && !otherSquare.isRevealed()) {
                    hitSquare(board, otherSquare);
                    return otherSquare;
                }
                otherSquare = GameBoardUtils.getSquareBelow(board, square);
                if (otherSquare != null && !otherSquare.isRevealed()) {
                    hitSquare(board, otherSquare);
                    return otherSquare;
                }

            }
            throw new RuntimeException("Computer has no idea where to hit. Spot number: 2");
        }

        squareToHit = getBestGuessedOccupiedSquare(board);

        if (squareToHit != null) { // very likely to be 0 at the beginning of data collection to DB
            hitSquare(board, squareToHit);
            return squareToHit;
        }

        squareToHit = getRandomlyGuessedOccupiedSquare(board);

        if (squareToHit != null) {
            hitSquare(board, squareToHit);
            return squareToHit;
        }

        throw new RuntimeException("Computer has no idea where to hit. Spot number: 3");
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
//        List<Square> hittableSquares = dao.getSquaresToHit(getCoveredSquares(board));
        List<Coordinates> hittableCoords = dao.getCoordinatesSortedByShipCountDesc(board.getId());

        List<Square> hittableSquares = new ArrayList<>();
        List<Square> remainingSquares = getCoveredSquares(board);

        if (hittableCoords == null || hittableCoords.isEmpty()) {
            return null;
        }
        for (Coordinates coordinates : hittableCoords) {
            Square square = GameBoardUtils.getSquare(remainingSquares, coordinates.getX(), coordinates.getY());
            if (square != null) {
                hittableSquares.add(square);
                // of course we can return here...
            }
        }

        if (hittableSquares.size() == 0) {
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

        for (Ship ship : board.getShips()) {
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
