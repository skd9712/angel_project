package com.angel.dao;

import com.angel.dto.InfoDTO;
import com.angel.dto.UserDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    private static UserDAO dao = new UserDAO();

    public static UserDAO getDao(){return dao;}

    private UserDAO(){}



    public UserDTO loginUser(Connection conn, String userID, String password) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append(" select userID, password            ");
        sql.append(" from users                           ");
        sql.append(" where userID = ? and password = ?  ");

        ResultSet rs = null;
        UserDTO dto = new UserDTO();
        try(PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            )
        {
            pstmt.setString(1, userID);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();

            while(rs.next()){
                dto.setUserID(rs.getString("userID"));
                dto.setPassword(rs.getString("password"));
            }
        }finally {
            if(rs!=null) try{rs.close();} catch (Exception e){}
        }
        return dto;
    }

    public int joinUser(Connection conn, UserDTO dto) throws SQLException{
        StringBuilder sql = new StringBuilder();
        sql.append(" insert into users ( userID        ");
        sql.append("                   , password      ");
        sql.append("                   , userName      ");
        sql.append("                   , address )     ");
        sql.append(" values (?, ?, ?, ? )              ");

        int result = 0;
        try(PreparedStatement pstmt = conn.prepareStatement(sql.toString());
        )
        {
            pstmt.setString(1, dto.getUserID());
            pstmt.setString(2, dto.getPassword());
            pstmt.setString(3, dto.getUserName());
            pstmt.setString(4, dto.getAddress());

           result = pstmt.executeUpdate();

        }
        return result;
    }

    public void modUser(Connection conn, UserDTO dto, String userNo) throws SQLException{
        StringBuilder sql = new StringBuilder();
        sql.append(" update users         ");
        sql.append(" set userID = ?       ");
        sql.append("    , password = ?    ");
        sql.append("    , userName = ?    ");
        sql.append("    , address = ?     ");
        sql.append(" where userNo = ?     ");

        try(PreparedStatement pstmt = conn.prepareStatement(sql.toString())){
            pstmt.setString(1, dto.getUserID());
            pstmt.setString(2, dto.getPassword());
            pstmt.setString(3, dto.getUserName());
            pstmt.setString(4, dto.getAddress());
            pstmt.setString(5, userNo);

            pstmt.executeUpdate();
        }
    }

    public void deleteUser(Connection conn, String userNo) throws SQLException{
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from users ");
        sql.append(" where userNo = ?  ");

        try(PreparedStatement pstmt = conn.prepareStatement(sql.toString())){
            pstmt.setString(1, userNo);
            pstmt.executeUpdate();
        }
    }

    public boolean findUser(Connection conn, String uid) throws SQLException{
        StringBuilder sql = new StringBuilder();
        sql.append(" select userID           ");
        sql.append(" from users              ");
        sql.append(" where userID = ?        ");

        boolean result = false;
        ResultSet rs = null;
        try(PreparedStatement pstmt = conn.prepareStatement(sql.toString());){
            pstmt.setString(1, uid);
            rs = pstmt.executeQuery();

            if (rs.next()){
                result = true;
            }
        }finally {
            if(rs!=null) try{rs.close();} catch (Exception e){}
        }
        return result;
    }

    public void addSellCount(Connection conn, String sessionId) throws SQLException{
        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE users                  ");
        sql.append(" SET sellCount = sellCount + 1 ");
        sql.append(" WHERE userID = ?              ");
        try(PreparedStatement pstmt = conn.prepareStatement(sql.toString());){
            pstmt.setString(1, sessionId);
            pstmt.executeUpdate();
        }
    }

    public InfoDTO findMyInfo(Connection conn, int myno)throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT (SELECT userName                     ");
        sql.append("           FROM users                        ");
        sql.append("          WHERE userNo = ?) AS userName      ");
        sql.append("       ,nvl(u.sellCount,0) AS sellCount      ");
        sql.append("       ,nvl(MAX(c.categoryName),?) AS hotCtg ");
        sql.append(" FROM users u INNER JOIN products p          ");
        sql.append("   ON u.userNo = p.sellerNo                  ");
        sql.append("   INNER JOIN categories c                   ");
        sql.append("   ON p.categoryNo = c.categoryNo            ");
        sql.append(" WHERE u.userNo = ?                          ");
        ResultSet rs = null;
        InfoDTO dto = new InfoDTO();
        try(PreparedStatement pstmt = conn.prepareStatement(sql.toString());){
            pstmt.setInt(1, myno);
            pstmt.setString(2,"comming soon");
            pstmt.setInt(3,myno);
            rs = pstmt.executeQuery();
            if (rs.next()){
                dto.setUserName(rs.getString("userName"));
                dto.setSellCount(rs.getInt("sellCount"));
                dto.setCategoryName(rs.getString("hotCtg"));
            }
        }finally {
            if(rs!=null) try{rs.close();} catch (Exception e){}
        }
        return dto;
    }

    public UserDTO userInfo(Connection conn, int uno) throws SQLException{
        StringBuilder sql = new StringBuilder();
        sql.append(" select userName, password, address   ");
        sql.append(" from users                    ");
        sql.append(" WHERE userNo = ?              ");

        UserDTO dto = new UserDTO();
        ResultSet rs = null;
        try(PreparedStatement pstmt = conn.prepareStatement(sql.toString());){
            pstmt.setInt(1, uno);
            rs = pstmt.executeQuery();

            while(rs.next()){
                dto.setUserName(rs.getString("userName"));
                dto.setPassword(rs.getString("password"));
                dto.setAddress(rs.getString("address"));
            }
        }
        return dto;
    }
}
