package org.zerock.board.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.QBoard;
import org.zerock.board.entity.QMember;
import org.zerock.board.entity.QReply;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {

    // 생성자
    public SearchBoardRepositoryImpl() {
        super(Board.class);
    }

    // search1() 기능 구현
    @Override
    public Board search1() {
        log.info("search1...........................");
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member)); // member로 조인
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        jpqlQuery.select(board, member.email, reply.count()).groupBy(board); // board 로 groupBy 쿼리 실행

        log.info("---------------------");
        log.info(jpqlQuery);
        log.info("----------------------");

        List<Board> result = jpqlQuery.fetch();
        return null;
    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {

        log.info("searchPage...........................");

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        // SELECT b, w, count(r) FROM Board b
        // LEFT JOIN b.writer w LEFT JOIN Reply r on ON r.board = b
        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count());

        // BooleanBuilder 인스턴스 생성
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        // board 엔터티의 bno 속성이 0보다 큰지를 검사하는 조건을 생성
        BooleanExpression expression = board.bno.gt(0L);
        // 위에서 생성한 조건을 BooleanBuilder에 추가
        booleanBuilder.and(expression);

        if(type != null) {
            String[] typeArr = type.split("");
            // 검색 조건을 작성하기
            BooleanBuilder conditionBuilder = new BooleanBuilder();

            for(String t : typeArr) {
                switch (t) {
                    case "t":
                        conditionBuilder.or(board.title.contains(keyword));
                        break;
                    case "w":
                        conditionBuilder.or(member.email.contains(keyword));
                        break;
                    case "c":
                        conditionBuilder.or(board.content.contains(keyword));
                        break;
                }
            }
            booleanBuilder.and(conditionBuilder);
        }

        tuple.where(booleanBuilder);
        tuple.groupBy(board);
        List<Tuple> result = tuple.fetch();
        log.info(result);

        // 정렬 및 count 수행 -> PagImpl 클래스 상속하여 사용
        long count = tuple.fetchCount();
        log.info("COUNT : " + count);

        return new PageImpl<Object[]>(
                result.stream().map(Tuple::toArray).collect(Collectors.toList()), pageable, count);
    }
}
