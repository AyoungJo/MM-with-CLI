package dao;

import common.DBManager;
import dto.ROLE;
import dto.UserDTO;
import exception.user.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {
    private static UserDAO instance = new UserDAOImpl();

    public static UserDAO getInstance() {
        return instance;
    }


    /**
     * 회원 전체 조회 함수
     */
    @Override
    public List<UserDTO> selectAll() throws UserLoadFailureException {
        String sql = "SELECT * FROM users";
        List<UserDTO> users = new ArrayList<>();

        try (Connection con = DBManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                users.add(
                        new UserDTO(
                                rs.getString("USER_ID"),
                                rs.getString("USER_PW"),
                                rs.getString("USER_NAME"),
                                ROLE.valueOf(rs.getString("ROLE")),
                                rs.getInt("CLASS_ID"))
                );
            }
        } catch (SQLException e) {
            throw new UserLoadFailureException();
        }

        return users;
    }
    @Override
    public List<UserDTO> selectByClass(long id) throws UserLoadFailureException{
        String sql = "SELECT * FROM users WHERE class_id = ?";
        List<UserDTO> users = new ArrayList<>();

        try (Connection con = DBManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                users.add(
                        new UserDTO(
                                rs.getString("USER_ID"),
                                rs.getString("USER_PW"),
                                rs.getString("USER_NAME"),
                                ROLE.valueOf(rs.getString("ROLE")),
                                rs.getInt("CLASS_ID"))
                );
            }
        } catch (SQLException e) {
            throw new UserLoadFailureException();
        }

        return users;
    }

    @Override
    /**
     * 회원 단건 조회 함수
     */
    public Optional<UserDTO> selectOne(String id) throws UserLoadFailureException {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (Connection con = DBManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new UserDTO(rs.getString("USER_ID"),
                        rs.getString("USER_PW"),
                        rs.getString("USER_NAME"),
                        ROLE.valueOf(rs.getString("ROLE")),
                        rs.getInt("CLASS_ID")
                ));
            }else{
                return Optional.ofNullable(null);
            }
        } catch (SQLException e) {
            throw new UserLoadFailureException();
        }

    }

    @Override
    /**
     * 회원가입 기능
     */
    public int join(UserDTO dto) throws UserJoinFailureException {
        String sql = "INSERT INTO users(user_id, user_pw, user_name, role) VALUES(?, ?, ?, ?)";
        try (Connection con = DBManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, dto.getUser_id());
            pstmt.setString(2, dto.getUser_pw());
            pstmt.setString(3, dto.getName());
            pstmt.setString(4, dto.getRole().toString());
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new UserJoinFailureException();
        }
    }

    @Override
    /**
     * 삭제 기능
     */
    public int delete(String id) throws UserDeleteFailureException {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection con = DBManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, id);
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserDeleteFailureException();
        }
    }

    @Override
    /**
     * 패스워드, 이름, 반, Role 수정 함수
     * 관리자 등록과 회원 가입 승인, 회원 정보 수정에 이용
     */
    public int update(UserDTO dto) throws UserUpdateFailureException {
        String sql = "UPDATE users SET user_pw = ?, user_name = ?, ROLE = ?, class_id = ? WHERE user_id = ?";
        try (Connection con = DBManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, dto.getUser_pw());
            pstmt.setString(2, dto.getName());
            pstmt.setString(3, dto.getRole().toString());
            pstmt.setInt(4, dto.getClass_id());
            pstmt.setString(5, dto.getUser_id());
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new UserUpdateFailureException();
        }
    }
}
