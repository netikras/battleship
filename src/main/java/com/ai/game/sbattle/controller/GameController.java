package com.ai.game.sbattle.controller;

import com.ai.game.sbattle.data.dto.MatchDto;
import com.ai.game.sbattle.data.dto.ShipDto;
import com.ai.game.sbattle.data.dto.SquareDto;
import com.ai.game.sbattle.data.model.GameBoard;
import com.ai.game.sbattle.data.model.GameMatch;
import com.ai.game.sbattle.service.GameService;
import com.ai.game.sbattle.utils.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by netikras on 17.5.15.
 */
@Controller
@RequestMapping(
        value = "/sbattle/match/{gameId}",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class GameController {


    @Resource
    private GameService gameService;

    @RequestMapping(
            value = "/hit/{squareId}",
            method = RequestMethod.POST
    )
    public boolean hit(
            @PathVariable(name = "gameId") String matchId,
            @PathVariable(name = "squareId") String squareId) {
        return false;
    }

    @RequestMapping(
            value = "/board/{boardId}/ships",
            method = RequestMethod.POST
    )
    public boolean submitShips(
            @PathVariable(name = "gameId") String gameId,
            @RequestBody List<ShipDto> shipDtos,
            @PathVariable(name = "boardId") String boardId) {
        return false;
    }

    public GameBoard getUpdatedBoard(String id) {
        return null;
    }

    @RequestMapping(
            value = "/poll",
            method = RequestMethod.GET
    )
    public MatchDto getUpdatedMatch(
            @PathVariable(name = "gameId") String gameId) {
        GameMatch match = gameService.getMatch(gameId);

        return ModelMapper.transform(match, new MatchDto());
    }

}
