package com.qgtechforum.dao;

import com.qgtechforum.model.Board;
import com.qgtechforum.utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BoardDao {
    //创建板块
    public Board createBoard(Board board) {
        String sql = "insert into boards (boardName, board_master_id ,is_approved) values (?, ?, ?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                 pstmt.setString(1, board.getBoardName());
                 pstmt.setInt(2, board.getMaster_id());
                 pstmt.setBoolean(3, board.isIs_approved());
                 pstmt.executeUpdate();
                 ResultSet rs = pstmt.getGeneratedKeys();
                 if (rs.next()) {
                     board.setId(rs.getInt(1));
                 }
                 return board;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //通过板块id查询板块
    public Board getBoardById(int boardId) {
        String sql = "select * from boards where id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                 pstmt.setInt(1, boardId);
                 ResultSet rs = pstmt.executeQuery();
                 if (rs.next()) {
                     Board board = new Board();
                     board.setId(rs.getInt("id"));
                     board.setBoardName(rs.getString("boardName"));
                     board.setMaster_id(rs.getInt("board_master_id"));
                     board.setIs_approved(rs.getBoolean("is_approved"));
                     return board;
                 }
             } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    //获取所有板块
    public List<Board> getAllBoards() {
        String sql = "select * from boards";
        List<Board> boards = new ArrayList<>();
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Board board = new Board();
                board.setId(rs.getInt("id"));
                board.setBoardName(rs.getString("boardName"));
                board.setMaster_id(rs.getInt("board_master_id"));
                board.setIs_approved(rs.getBoolean("is_approved"));
                boards.add(board);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return boards;
    }

    //获取被审核通过的板块
    public List<Board> getApprovedBoards() {
        String sql = "select * from boards where is_approved = true";
        List<Board> boards = new ArrayList<>();
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Board board = new Board();
                board.setId(rs.getInt("id"));
                board.setBoardName(rs.getString("boardName"));
                board.setMaster_id(rs.getInt("board_master_id"));
                board.setIs_approved(rs.getBoolean("is_approved"));
                boards.add(board);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return boards;
    }

    //管理员权限：批准板块通过
    public boolean approveBoard(int boardId) {
        String sql = "update boards set is_approved = true where id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                 pstmt.setInt(1, boardId);
                 int i = pstmt.executeUpdate();
                 return i > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //管理员权限：删除帖子
    public boolean deletePostInBoard(int postId,int boardId) {
        String sql = "delete from posts where id = ? and board_id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                 pstmt.setInt(1, postId);
                 pstmt.setInt(2, boardId);
                 int i = pstmt.executeUpdate();
                 return i > 0;
             } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //吧主权限：封禁某个用户在该板块发布帖子的权利
    public boolean banUserInBoard(int userId,int boardId) {
        String sql = "insert into board_bans (user_id, board_id) values (?,?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                 pstmt.setInt(1, userId);
                 pstmt.setInt(2, boardId);
                 int i = pstmt.executeUpdate();
                 return i > 0;
             } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //根据关键字搜索板块
    public List<Board> searchBoards(String keyword) {
        String sql = "select * from boards where boardName like ?";
        List<Board> boards = new ArrayList<>();
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            //%是通配符，用于匹配任意字符，在keyword两边加上%，就可以实现模糊查询，即查询所有包含关键字的板块
                 pstmt.setString(1, "%" + keyword + "%");
                 ResultSet rs = pstmt.executeQuery();
                 while (rs.next()) {
                     Board board = new Board();
                     board.setId(rs.getInt("id"));
                     board.setBoardName(rs.getString("boardName"));
                     board.setMaster_id(rs.getInt("board_master_id"));
                     board.setIs_approved(rs.getBoolean("is_approved"));
                     boards.add(board);
                 }
             } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return boards;
    }

    //获取热门板块（根据板块内帖子的数量决定板块的热门程度，最后按照帖子数量的多少降序排序板块，将板块添加到集合之后返回）
    public List<Board> getHotBoards(int limit) {
        //SQL语句需要用到聚合函数count统计板块帖子的数量，还要给count起别名：post_count；还需要查看帖子表和板块表，需要用到join连接
        //给帖子表起别名为p，给板块表起别名为b
        String sql = "select b.*,count(p.id) as post_count from boards b " +
                "left join posts p on b.id = p.board_id " +
                "group by b.id order by post_count desc limit ?";
        List<Board> boards = new ArrayList<>();
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                 pstmt.setInt(1, limit);
                 ResultSet rs = pstmt.executeQuery();
                 while (rs.next()) {
                     Board board = new Board();
                     board.setId(rs.getInt("id"));
                     board.setBoardName(rs.getString("boardName"));
                     board.setMaster_id(rs.getInt("board_master_id"));
                     board.setIs_approved(rs.getBoolean("is_approved"));
                     boards.add(board);
                 }
             } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return boards;
    }
}
