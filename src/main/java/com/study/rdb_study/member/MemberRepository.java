package com.study.rdb_study.member;

import com.study.rdb_study.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Member> memberRowMapper = (rs, rowNum) -> Member.builder()
            .memberId(rs.getLong("member_id"))
            .name(rs.getString(("name")))
            .email(rs.getString("email"))
            .address(rs.getString("address"))
            .status(MemberStatus.valueOf(rs.getString("status")))
            .role(MemberRole.valueOf(rs.getString("role")))
            .joinDate(rs.getTimestamp("join_date").toLocalDateTime())
            .build();

    private final RowMapper<Member> memberRowMapperWithPassword = (rs, rowNum) -> Member.builder()
            .memberId(rs.getLong("member_id"))  // CustomUserDetails에 넘겨야 하므로 추가
            .email(rs.getString("email"))
            .password(rs.getString("password"))
            .status(MemberStatus.valueOf(rs.getString("status")))
            .role(MemberRole.valueOf(rs.getString("role")))
            .build();

    // 회원가입
    public Member save(Member member) {
        String sql = "insert into members (name, email, password, address, status, role) values (?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getEmail());
            pstmt.setString(3, member.getPassword());
            pstmt.setString(4, member.getAddress());
            pstmt.setString(5, member.getStatus().name());
            pstmt.setString(6, member.getRole().name());
            return pstmt;
        }, keyHolder);

        return findById(keyHolder.getKey().longValue())
                .orElseThrow(() -> new NotFoundException("고객 조회 실패"));
    }

    // 로그인 후 현재 사용자 정보 조회용 (password 제외)
    public Optional<Member> findByEmailPublic(String email) {
        String sql = "select member_id, name, email, address, status, role, join_date from members where email=?";
        return jdbcTemplate.query(sql, memberRowMapper, email)
                .stream()
                .findFirst();
    }

    // Security
    public Optional<Member> findByEmail(String email) {
        String sql = "select member_id, email, password, status, role from members where email=?";

        return jdbcTemplate.query(sql, memberRowMapperWithPassword, email)
                .stream()
                .findFirst();
    }

    // 회원 조회
    public Optional<Member> findById(Long id) {
        String sql = "select member_id, name, email, address, status, role, join_date from members where member_id=?";

        List<Member> result = jdbcTemplate.query(sql, memberRowMapper, id);
        return result.stream().findFirst();
    }

    // 현재 비밀번호 검증(관리자용?)
    public Optional<String> findPasswordById(Long id) {
        String sql = "select password from members where member_id=?";

        List<String> result = jdbcTemplate.query(sql, (rs, rowNum) -> {
            return rs.getString(1);
            } ,id);

        return result.stream().findFirst();
    }

    // 회원정보 수정
    public void update(Member member) {
        String sql = "update members set email=?, address=? where member_id=?";

        jdbcTemplate.update(sql, member.getEmail(), member.getAddress(), member.getMemberId());
    }

    // 비밀번호 변경
    public void updatePassword(Long id, String newPassword) {
        String sql = "update members set password=? where member_id=?";

        jdbcTemplate.update(sql, newPassword, id);
    }

    // 회원탈퇴 (INACTIVE)
    public void updateStatus(Long id, MemberStatus status) {
        String sql = "update members set status=? where member_id=?";

        jdbcTemplate.update(sql, status.name(), id);
    }

    // update 시 회원 존재 검증
    public boolean existsById(Long id) {
        String sql = "select count(*) from members where member_id=?";

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);

        return count != null && count > 0;
    }
}