package com.inyro.api.domain.admin.repository;

import com.inyro.api.domain.member.entity.*;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomAdminRepositoryImpl implements CustomAdminRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Member> findAllMembers(MemberSortType sort, OrderType order, Pageable pageable) {
        QMember member = QMember.member;

        JPAQuery<Member> query = queryFactory
                .selectFrom(member);

        if (sort == null) {
            sort = MemberSortType.DEFAULT;
        }
        if (order == null) {
            order = OrderType.ASC;
        }
        switch (sort) {
            case NAME -> query.orderBy(order == OrderType.ASC ? member.name.asc() : member.name.desc());
            case SNO -> query.orderBy(order == OrderType.ASC ? member.sno.asc() : member.sno.desc());
            case DEPARTMENT -> query.orderBy(order == OrderType.ASC ? member.dept.asc() : member.dept.desc());
            default -> query.orderBy(
                    new CaseBuilder()
                            .when(member.status.eq(Status.ENROLLED)).then(1)
                            .when(member.status.eq(Status.LEAVE)).then(2)
                            .when(member.status.eq(Status.WITHDRAWN)).then(3)
                            .otherwise(4)
                            .asc(),
                    member.name.asc()
            );
        }

        List<Member> members = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(member.count())
                .from(member)
                .fetchOne();

        return new PageImpl<>(members, pageable, total != null ? total : 0L);
    }

}
