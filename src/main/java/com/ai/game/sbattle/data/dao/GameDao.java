package com.ai.game.sbattle.data.dao;

import com.ai.game.sbattle.data.model.*;

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


    public List<Coordinates> getCoordinatesSortedByHitCountDesc() {
        /*
select
  i.address, count(r.electronic) as el_count
from
  inbox_user iu
  join inbox i
    on i.id = iu.inbox_id
  left join message m
    on m.sender_box_id = i.id
  left join recipient r
    on r.message_id = m.id

where
  i.type = 'PERSON'
  and ( r.electronic = 1 or r.electronic is null)

group by i.address
order by el_count desc
;
         */


        /*
        select
            c.id,
            count(sq.revealed) as hit_count
        from
            coordinates c
            left join square sq
                on sq.coord_id = c.id
        where
            sq.revealed = 1
            or sq.revealed is null
        group by c.id
        order by hit_count desc
         */


        String queryString = "" +
                "select" +
                "    c.*" +
                "from" +
                "    coordinates c" +
                "    join (" +
                "        select" +
                "            c.id," +
                "            count(sq.revealed) as hit_count" +
                "        from" +
                "            coordinates c" +
                "            left join square sq" +
                "                on sq.coord_id = c.id" +
                "        where" +
                "            sq.revealed = 1" +
                "            or sq.revealed is null" +
                "        group by c.id" +
                "        order by hit_count desc" +
                "    ) stats" +
                "        on stats.id = c.id" +
                "order by stats.hit_count desc";
        return null;
    }

    public List<Square> getSquaresToHit(List<String> candidatesIds) {
        /*
        select paper, electronic, count(*) as occurrences from recipient group by paper, electronic order by occurrences desc;
        select
            c.id,
            count(*) as total_count
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


    public List<Coordinates> getCoordinatesSortedByShipCountDesc() {
        /*

        NOT returning squares having 0 ships ever hosted on them. If square has not been used it should be picked up randomly.
        Otherwise it would be just stupid picking squares one-by-one in the same order...

        select
            c.*
        from
            coordinates c
            join (
                select
                    c.id,
                    count(*) as total_count
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
                group by c.id
                order by total_count desc
            ) stats
                on stats.id = c.id
        order by stats.total_count desc
        ;
         */


        List<Coordinates> coords = null;



        return coords;
    }



}
