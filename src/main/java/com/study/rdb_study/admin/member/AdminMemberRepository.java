package com.study.rdb_study.admin.member;

import com.study.rdb_study.member.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AdminMemberRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<AdminMemberResponse> adminMemberResponseRowMapper = (rs, rowNum) -> AdminMemberResponse.builder()
            .memberId(rs.getLong("member_id"))
            .name(rs.getString("name"))
            .email(rs.getString("email"))
            .address(rs.getString("address"))
            .status(rs.getString("status"))
            .role(MemberRole.valueOf(rs.getString("role")))
            .joinDate(rs.getTimestamp("join_date").toLocalDateTime())
            .totalOrderCount(rs.getInt("total_order_count"))
            .totalPrice(rs.getInt("total_price"))
            .lastOrderDate(rs.getTimestamp("last_order_date") != null
                    ? rs.getTimestamp("last_order_date").toLocalDateTime() : null)
            .build();

    // 관리자용
    public List<AdminMemberResponse> findAll() {
        String sql = """
        select m.member_id,
               m.name,
               m.email,
               m.address,
               m.status,
               m.role,
               m.join_date,
                ifnull(t.total_order_count, 0) as total_order_count,
                ifnull(t.total_price, 0) as total_price,
                t.last_order_date as last_order_date
               from members m
               left join (
                select
               		o.member_id,
               		sum(p.price * oi.quantity) as total_price,
               		count(distinct o.order_id) as total_order_count,
               		max(o.order_date) as last_order_date
               	from orders o
                    join order_items oi on o.order_id = oi.order_id
                    join products p on oi.product_id = p.product_id
                where o.status != 'CANCELLED'
                group by o.member_id
               ) t on m.member_id = t.member_id;
        """;

        return jdbcTemplate.query(sql, adminMemberResponseRowMapper);
    }
}
