package com.ai.game.sbattle.data.dao;

import com.ai.game.sbattle.data.model.*;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by netikras on 17.5.16.
 */
@Component
public class GameDao {

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    private Session currentSession = null;


    private Session getCurrentSession() {
        Session session = currentSession;

        if (session == null || !session.isOpen()) {
            try {
                session = sessionFactory.getCurrentSession();

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (session == null || !session.isOpen()) {
                session = sessionFactory.openSession();
            }
        }



        return session;
    }


    private Criteria buildCriteria(Class model) {
        return getCurrentSession().createCriteria(model);
    }

    private Criteria buildCriteria(Class model, String alias) {
        return getCurrentSession().createCriteria(model, alias);
    }



    public List<Coordinates> getAllCoordinates() {
        return buildCriteria(Coordinates.class)
                .addOrder(Order.desc("y"))
                .addOrder(Order.desc("x"))
                .list();
    }


    public GameMatch getMatchById(String id) {
        System.out.println("Getting match [dao]: " + id);
        return getCurrentSession().get(GameMatch.class, id);
    }

    public Square getSquareById(String id) {
        return getCurrentSession().get(Square.class, id);
    }


    public String save(GameMatch match) {
        return (String) getCurrentSession().save(match);
    }

    public String save(GameBoard board) {
        return (String) getCurrentSession().save(board);
    }

    public void update(GameMatch match) {
        getCurrentSession().update(match);
    }

    public void update(Ship ship) {
        getCurrentSession().update(ship);
    }

    public void update(Square square) {
        getCurrentSession().update(square);
    }

    public void update(GameBoard board) {
        getCurrentSession().update(board);
    }


    public List<Coordinates> getCoordinatesSortedByHitCountAsc() {
        // FIXME exclude current game
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
                "order by stats.hit_count asc";
        return null;
    }

    public List<Square> getSquaresToHit(List<String> candidatesIds) {
        // FIXME exclude current game
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
        // FIXME exclude current game
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


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }

    public GameBoard getBoard(String boardId) {
        return getCurrentSession().get(GameBoard.class, boardId);
    }

    public Player getPlayer(String playerId) {
        return getCurrentSession().get(Player.class, playerId);
    }
}
