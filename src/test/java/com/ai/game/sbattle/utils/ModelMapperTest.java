package com.ai.game.sbattle.utils;

import com.ai.game.sbattle.data.dto.*;
import com.ai.game.sbattle.data.model.*;
import com.ai.game.sbattle.service.ComputerPlayerService;
import com.ai.game.sbattle.service.GameService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by netikras on 17.5.15.
 */
public class ModelMapperTest {

    GameService gameService;
    ComputerPlayerService computerPlayerService;

    @Before
    public void init() {
        gameService = new GameService();
        computerPlayerService = new ComputerPlayerService();
    }

    @Test
    public void transformTest() {

        GameBoard board = new GameBoard();
        GameMatch match = new GameMatch();
        GameBoardDto boardDto = new GameBoardDto();
        PlayerDto playerDto = new PlayerDto();
        SquareDto squareDto = new SquareDto();
        MatchDto matchDto = new MatchDto();
        ShipDto shipDto = new ShipDto();

        Player boardOwner = new Player();
        boardOwner.setId("player 1");
        boardOwner.setBoard(board);
        boardOwner.setCreatedOn(new Date());
        boardOwner.setUpdatedOn(null);

        Ship ship = new Ship();
        ship.setId("Ship 1");
        ship.setType(Ship.ShipType.BATTLESHIP);
        ship.setBoard(board);
        ship.setKilled(false);

        List<Ship> ships = new ArrayList<>();
        ships.add(ship);

        Square sq1 = new Square();
        Square sq2 = new Square();
        List<Square> squares = new ArrayList<>();

        sq1.setId("Square 1");
        sq1.setBoard(board);
        sq1.setRevealed(false);
        sq1.setHostedShip(ship);
        sq1.setCoordinates(new Coordinates(3, 3));

        sq2.setId("Square 2");
        sq2.setBoard(board);
        sq2.setRevealed(true);
        sq2.setHostedShip(ship);
        sq2.setCoordinates(new Coordinates(3, 4));

        squares.add(sq1);
        squares.add(sq2);

        ship.setSquares(squares);

        board.setCreatedOn(new Date());
        board.setId("board 1");
        board.setUpdatedOn(null);
        board.setBoardOwner(boardOwner);
        board.setShips(ships);

        match.setId("Match #115");
        match.setCreatedOn(new Date());
        match.setPlayerA(boardOwner);


        System.out.println(ModelMapper.transform(board, boardDto));
        System.out.println(ModelMapper.transform(boardOwner, playerDto));
        System.out.println(ModelMapper.transform(ship, shipDto));
        System.out.println(ModelMapper.transform(sq1, squareDto));
        System.out.println(ModelMapper.transform(sq2, squareDto));
        System.out.println(ModelMapper.transform(match, matchDto));
    }


    @Test
    public void applyDtoUpdateTest() {
        GameBoard board = gameService.createNewBoard(gameService.createNewPlayer(), 10);
        GameBoardDto dto = ModelMapper.transform(board, new GameBoardDto());
        Player otherPlayer = gameService.createNewPlayer();
        PlayerDto playerDto = ModelMapper.transform(otherPlayer, new PlayerDto());
        dto.setPlayerId(playerDto.getId());

        GameBoardUtils.fillWithShipsRandomly(board);


        Ship ship = board.getShips().get(0);
        ShipDto shipDto = ModelMapper.transform(ship, new ShipDto());
        Ship otherShip = ModelMapper.apply(new Ship(), shipDto);


        ModelMapper.apply(board, dto);

        System.out.println("done!");
    }

}
