package com.ai.game.sbattle.data.dao;

import com.ai.game.sbattle.data.model.GameBoard;
import com.ai.game.sbattle.data.model.GameMatch;
import com.ai.game.sbattle.data.model.Ship;
import com.ai.game.sbattle.data.model.Square;

import java.util.List;

/**
 * Created by netikras on 17.5.16.
 */
public class GameDao {

    public GameMatch getMatchById(String id) {
        return null;
    }

    public Square getSquareById(String id) {
        return null;
    }


    public String save(GameMatch match) {
        return "";
    }

    public String save(GameBoard board) {
        return "";
    }

    public void update(GameMatch match) {

    }

    public void update(Ship ship) {

    }

    public void update(Square square) {

    }

    public void update(GameBoard board) {

    }


    public List<Square> getSquaresToHit(List<Square> candidates) {
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


        List<Square> squares = null;



        return squares;
    }



}
