package com.ai.game.sbattle.controller;

import com.ai.game.sbattle.data.dto.*;
import com.ai.game.sbattle.data.model.GameBoard;
import com.ai.game.sbattle.data.model.GameMatch;
import com.ai.game.sbattle.data.model.Player;
import com.ai.game.sbattle.service.ComputerPlayerService;
import com.ai.game.sbattle.service.GameService;
import com.ai.game.sbattle.utils.ConsoleVisualizer;
import com.ai.game.sbattle.utils.GameBoardUtils;
import com.ai.game.sbattle.utils.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by netikras on 17.5.15.
 */
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(
        value = "/match/{gameId}",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE
)
public class DataController {

    private static Logger logger = LoggerFactory.getLogger(DataController.class);

    @Resource
    private GameService gameService;

    @Resource
    private ComputerPlayerService computerPlayerService;

    @RequestMapping(
            value = "/board/{boardId}/ships",
            method = RequestMethod.POST
    )
    @ResponseBody
    public boolean submitShips(
            @PathVariable(name = "gameId") String gameId,
            @RequestBody List<ShipDto> shipDtos,
            @PathVariable(name = "boardId") String boardId) {
        return gameService.submitBoardShips(shipDtos, boardId);
    }

    @RequestMapping(
            value = "/board/{boardId}/hitme",
            method = RequestMethod.GET
    )
    @ResponseBody
    public SquareDto opponentTurn(
            @PathVariable(name = "gameId") String gameId,
            @PathVariable(name = "boardId") String boardId) {
        return gameService.opponentTurn(gameId, boardId);
    }

    @RequestMapping(
            value = "/board/{boardId}",
            method = RequestMethod.GET
    )
    @ResponseBody
    public GameBoardDto getUpdatedBoard(
            @PathVariable(name = "gameId") String matchId,
            @PathVariable(name = "boardId") String boardId
    ) {
        GameBoard board = gameService.getBoard(boardId);
        return ModelMapper.transform(board, new GameBoardDto());
    }

    @RequestMapping(
            value = "/poll",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseBody
    public MatchDto getUpdatedMatch(
            @PathVariable(name = "gameId") String gameId) {
        GameMatch match = gameService.getMatch(gameId);
        MatchDto matchDto = ModelMapper.transform(match, new MatchDto());

        System.out.println("Board A");
//        System.out.println(matchDto.getPlayerA().getBoard());
        ConsoleVisualizer.drawBoard(match.getPlayerA().getBoard());
        System.out.println("Board B");
//        System.out.println(matchDto.getPlayerB().getBoard());
        ConsoleVisualizer.drawBoard(match.getPlayerB().getBoard());

        return matchDto;
    }

    @RequestMapping(
            value = "/player/{playerId}",
            method = RequestMethod.GET
    )
    @ResponseBody
    public PlayerDto getUpdatedPlayer(
            @PathVariable(name = "gameId") String matchId,
            @PathVariable(name = "playerId") String playerId
    ) {
        Player player = gameService.getPlayer(playerId);
        return ModelMapper.transform(player, new PlayerDto());
    }


    @RequestMapping(
            value = "/player/{playerId}/user/{username}",
            method = RequestMethod.GET
    )
    @ResponseBody
    public void claimPlayer(
            @PathVariable(name = "gameId") String matchId,
            @PathVariable(name = "playerId") String playerId,
            @PathVariable(name = "username") String username
    ) {
        gameService.assignPlayerToUser(playerId, username);
    }


    @RequestMapping(
            value = "/player/{playerId}/robot",
            method = RequestMethod.GET
    )
    @ResponseBody
    public void refusePlayer(
            @PathVariable(name = "gameId") String matchId,
            @PathVariable(name = "playerId") String playerId
    ) {
        gameService.assignPlayerToRobot(playerId);
    }


    @RequestMapping(
            value = "/open/square/{squareId}",
            method = RequestMethod.GET
    )
    @ResponseBody
    public SquareDto openSquare(
            @PathVariable(name = "gameId") String matchId,
            @PathVariable(name = "squareId") String squareId
    ) {

        return gameService.openSquare(squareId);
    }



    @RequestMapping(
            value = "/test",
            method = RequestMethod.GET
    )
    @ResponseBody
    public String test(
            @PathVariable(name = "gameId") String matchId
    ) {
        return "Hello World! Match ID: " + matchId;
    }


    @RequestMapping(
            value = "/test/new/board",
            method = RequestMethod.GET
    )
    @ResponseBody
    public GameBoardDto testNewBoard(
            @PathVariable(name = "gameId") String matchId
    ) {
        GameBoard board = gameService.buildNewBoard();
        board.setBoardOwner(gameService.createNewPlayer());
        return ModelMapper.transform(board, new GameBoardDto());
    }

    @RequestMapping(
            value = "/test/new/match",
            method = RequestMethod.GET
    )
    @ResponseBody
    public MatchDto testNewMatch(
            @PathVariable(name = "gameId") String matchId
    ) {
        GameMatch match = gameService.buildNewMatch();

        computerPlayerService.fillBoard(match.getPlayerB().getBoard(), 0);
        gameService.updateBoardSetting(match.getPlayerB().getBoard());

        System.out.println("Starting new match. Player A board:");
        ConsoleVisualizer.drawBoard(match.getPlayerA().getBoard());

        System.out.println("Starting new match. Player B board:");
        ConsoleVisualizer.drawBoard(match.getPlayerB().getBoard());


        GameBoardUtils.fillWithShipsRandomly(match.getPlayerA().getBoard(), true);
//        GameBoardUtils.fillWithShipsRandomly(match.getPlayerB().getBoard());


        return ModelMapper.transform(match, new MatchDto());
    }

}
