package com.ai.game.sbattle.controller;

import com.ai.game.sbattle.data.dto.GameBoardDto;
import com.ai.game.sbattle.data.dto.MatchDto;
import com.ai.game.sbattle.data.dto.PlayerDto;
import com.ai.game.sbattle.data.dto.ShipDto;
import com.ai.game.sbattle.data.model.GameBoard;
import com.ai.game.sbattle.data.model.GameMatch;
import com.ai.game.sbattle.data.model.Player;
import com.ai.game.sbattle.service.GameService;
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
@RestController
@RequestMapping(
        value = "/game/match/{gameId}",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE
)
public class DataController {

    private static Logger logger = LoggerFactory.getLogger(DataController.class);

    @Resource
    private GameService gameService;

    @RequestMapping(
            value = "/hit/{squareId}",
            method = RequestMethod.POST
    )
    @ResponseBody
    public boolean hit(
            @PathVariable(name = "gameId") String matchId,
            @PathVariable(name = "squareId") String squareId) {
        return false;
    }

    @RequestMapping(
            value = "/board/{boardId}/ships",
            method = RequestMethod.POST
    )
    @ResponseBody
    public boolean submitShips(
            @PathVariable(name = "gameId") String gameId,
            @RequestBody List<ShipDto> shipDtos,
            @PathVariable(name = "boardId") String boardId) {
        return false;
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
        logger.info("HEEEELLLOOOOOO");
        GameMatch match = gameService.getMatch(gameId);

        return ModelMapper.transform(match, new MatchDto());
//        return new MatchDto();
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
        return ModelMapper.transform(match, new MatchDto());
    }

}
