package com.ai.game.sbattle.data.dao;

import com.ai.game.sbattle.data.model.*;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
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
        getCurrentSession().saveOrUpdate(board);
    }


    public List<Coordinates> getCoordinatesSortedByHitCountAsc(String currBoardId) {
        // FIXME exclude current game


        String queryString = "" +
                "select " +
                "    c.* " +
                "from " +
                "    coordinates c " +
                "    join ( " +
                "        select " +
                "            c.id, " +
                "            count(sq.revealed) as hit_count " +
                "        from " +
                "            coordinates c " +
                "            left join board_square sq " +
                "                on sq.coord_id = c.id " +
                "            left join game_board b " +
                "                on b.id = sq.board_id " +
                "                and b.id != :currBoard " +
                "             left join players p " +
                "                on p.id = b.player_id " +
                "                and p.robot = true " +
                "        where " +
                "            sq.revealed = true " +
                "            or sq.revealed is null " +
                "        group by c.id " +
                "        order by hit_count desc " +
                "    ) stats " +
                "        on stats.id = c.id " +
                "order by stats.hit_count asc "
                ;

        NativeQuery query = getCurrentSession().createNativeQuery(queryString);
        query.setParameter("currBoard", currBoardId);


        return query.addEntity(Coordinates.class).list();
    }

    public List<Square> getSquaresToHit(List<String> candidatesIds) {
        // FIXME exclude current game


        List<Square> squares = null;


        return squares;
    }


    public List<Coordinates> getCoordinatesSortedByShipCountDesc(String currentBoardId) {
        // FIXME exclude current game

        String queryStr = "" +
                "SELECT " +
                "   c .* " +
                "FROM " +
                "    Coordinates c " +
                "    JOIN ( " +
                "        SELECT " +
                "            c.id, " +
                "            count(*) as total_count " +
                "        FROM " +
                "            board_square sq " +
                "            JOIN Coordinates c " +
                "                on c.id = sq.coord_id " +
                "            JOIN game_board b " +
                "                on b.id = sq.board_id " +
                "                and b.id != :currBoard " +
                "            JOIN Players p " +
                "                on p.id = b.player_id " +
                "                and p.robot = false " +
                "        WHERE " +
                "            sq.hosted_ship_id is not null " +
                "        GROUP BY c.id " +
                "        ORDER BY total_count desc " +
                "    ) stats " +
                "        on stats.id = c.id " +
                "ORDER BY stats.total_count DESC"
                ;
        NativeQuery query = getCurrentSession().createNativeQuery(queryStr);
        query.setParameter("currBoard", currentBoardId);

        List<Coordinates> coords = query.addEntity(Coordinates.class).list();

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

    public void update(Player player) {
        getCurrentSession().update(player);
    }
}
