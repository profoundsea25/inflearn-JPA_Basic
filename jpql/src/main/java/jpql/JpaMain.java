package jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        // EMF는 로딩 시점에 딱 하나만 만들어야 한다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        // 쿼리 날릴거면 EM 반드시 필요
        EntityManager em = emf.createEntityManager();
        // JPA 데이터 변경을 위해 필요
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("member1");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("member1");
            member3.setTeam(teamB);
            em.persist(member3);

            // 반환 타입이 명확하면 TypedQuery
            TypedQuery<Member> typedQuery = em.createQuery("select m from Member m", Member.class);
            // 반환 타입이 명확하지 않으면 Query
            Query query = em.createQuery("select m.username, m.age from Member m");

            // 결과 값을 리스트로
            List<Member> resultList = typedQuery.getResultList();
            // 딱 하나만
            Member singleResult = typedQuery.getSingleResult();


            TypedQuery<Member> query1 = em.createQuery("select m from Member m where m.username = :username", Member.class);
            query1.setParameter("username", "member1");
            Object singleResult1 = query.getSingleResult();
            // 위의 방식 보다 아래 방식(체인)이 더 많이 쓰인다.
            Member singleResult2 = em.createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", "member1")
                    .getSingleResult();
            // 중요 : JPQL로 조회된 객체들은 모두 영속성 컨텍스트에서 관리된다. 값을 수정하면 update 쿼리가 나감.

            // 여러 값 조회하기 by DTO -> 나중에 QueryDSL 사용하면 편하다
            List<MemberDTO> result = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                    .getResultList();

            // 페이징 API
            List<Member> resultList1 = em.createQuery("select m from Member m order by m.age desc", Member.class)
                    .setFirstResult(0) // 어디서부터
                    .setMaxResults(10) // (최대) 몇개 가져올래?
                    .getResultList();

            // 내부 조인 (연관관계 엔티티가 없으면 조회 불가능), inner join 대신 join만 써도 됨
            String query2 = "select m from Member m inner join m.team t";
            List<Member> result2 = em.createQuery(query2, Member.class)
                            .getResultList();

            // 외부 조인 left (outer) join (연관관계 엔티티가 없더라도 조회)
            String query3 = "select m from Member m left join m.team t";
            List<Member> result3 = em.createQuery(query3, Member.class)
                    .getResultList();

            // 세타 조인 : 연관관계가 없는 두 테이블에서 데이터를 뽑아오고 싶을 때
            String query4 = "select m from Member m, Team t where m.username = t.name";
            List<Member> result4 = em.createQuery(query4, Member.class)
                    .getResultList();

            // ON 활용하기
            // 조인 대상 필터링 (예 : 회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인)
            String query5 = "select m,t from Member m left join Team t on t.name = 'teamA'";
            List<Member> result5 = em.createQuery(query5, Member.class)
                    .getResultList();

            // 연관관계 없는 엔티티 외부 조인 (회원의 이름과 팀의 이름이 같은 대상 외부 조인)
            String query6 = "select m,t from Member m left join Team t on m.username = t.name";
            List<Member> result6 = em.createQuery(query6, Member.class)
                    .getResultList();

            // JPQL에서 enum을 표현하려면 패키지명을 포함해야 한다.
            String query7 = "select m.username, 'HELLO', true from Member m where m.type = jpql.MemberType.ADMIN";
            List result7 = em.createQuery(query7)
                    .getResultList();
            // 혹은, 파라미터 설정하기
            String query8 = "select m.username, 'HELLO', true from Member m where m.type = :userType";
            List result8 = em.createQuery(query8)
                    .setParameter("userType", MemberType.ADMIN)
                    .getResultList();

            // 잘 쓰지는 않지만, Item과 Book, Album, Movie의 관계에서, Book인 타입만 가져오고 싶다면,
            em.createQuery("select i from Item i where type(i) = Book", Item.class).getResultList();

            // 조건식 - CASE 식 -> 나중에 QueryDSL 로 편하게 작성 가능
            // 기본 CASE 식
            String query9 = "select case when m.age <= 10 then '학생요금' when m.age >= 60 then '경로요금' else '일반요금' end from Member m ";
            List<String> result9 = em.createQuery(query9, String.class)
                    .getResultList();
            // COALESCE : 하나씩 조회해서 null 이 아니면 반환
            String query10 = "select coalesce(m.username, '이름 없는 회원') from Member m";
            List<String> result10 = em.createQuery(query10, String.class)
                    .getResultList();
            // NULLIF : 두 값이 같으면 null 반환, 다르면 첫 번째 값 반환
            String query11 = "select coalesce(m.username, 'member1') from Member m";
            List<String> result11 = em.createQuery(query11, String.class)
                    .getResultList(); // NULL 반환


            // 경로 표현식
            // 상태 필드 : 경로 탐색 끝, 탐색 X (m.username)
            String query12 = "select m.username From Member m";

            // 단일 값 연관 경로 : 묵시적 내부 조인(inner join) 발생, 탐색 O (m.team 에서 . 찍고 더 탐색 가능)
            // 묵시적 내부 조인이 편해 보이지만 쿼리(성능) 튜닝이 어렵다. join 은 조심히 사용해야 한다.
            // 작성한 쿼리에서는 join을 쓰지 않았는데, 실제로는 join 쿼리가 나간다!
            String query13 = "select m.team From Member m";

            // 컬렉션 값 연관 경로 : 묵시적 내부 조인 발생, 단일 값 연관경로와 다르게 탐색 불가능
            // (컬렉션이라서 어떤 member 를 탐색해야 할지 정해지지 않았기 때문)
            String query14 = "select t.members From Team t";
            String query14_ = "select t.members.username From Team t"; // 잘못된 쿼리. t.member.username 조회 불가능
            // 탐색을 하고 싶으면 from 절에서 명시적 조인을 통해 별칭을 얻으면 별칭으로 탐색 가능
            String query15 = "select m.username From Team t join t.members m";

           // 조언 : 묵시적 조인쓰지 말고 명시적 조인을 써라. 묵시적 조인은 성능 튜닝이 어렵기 때문!


            // Fetch Join (즉시 로딩과 비슷)
            // 지연로딩을 설정해도, 페치 조인이 우선이기 때문에 즉시 로딩된다.
            String query16 = "select m from Member m join fetch m.team";
            List<Member> result16 = em.createQuery(query16, Member.class)
                    .getResultList(); // NULL 반환
            // query16은 아래의 sql과 같다
            String query17 = "select m.*, t.* from Member m inner join team t on m.team_id=t.id";

            // 컬렉션 페치 조인
            String query18 = "select t from Team t join fetch t.members where t.name = 'teamA'";
            // 아래 sql과 같음
            String query19 = "select t.*, m.* from Team t inner join member m on t.id = m.team_id where t.name = 'teamA'";


            // 엔티티 직접 사용
            // 기본 키 값 사용
            // 엔티티를 파라미터로 전달
            String query20 = "select m from Member m where m = :member";
            List<Member> result20 = em.createQuery(query20, Member.class).setParameter("member", member).getResultList();

            // 식별자를 파라미터로 전달
            String query21 = "select m from Member m where m.id = :memberId";
            List<Member> resultList21 = em.createQuery(query21, Member.class).setParameter("memberId", memberId).getResultList();

            // 외래 키 값 사용
            Team team = em.find(Team.class, 1L);

            // 엔티티를 파라미터로 전달
            String query22 = "select m from Member m where m.team = :team";
            List<Member> resultList22 = em.createQuery(query22, Member.class).setParameter("team", team).getResultList();

            // 식별자를 파라미터로 전달
            String query23 = "select m from Member m where m.team.id = :teamId";
            List<Member> resultList23 = em.createQuery(query23, Member.class).setParameter("teamId", teamId).getResultList();


            // NamedQuery
            List<Member> resultList24 = em.createNamedQuery("Member.findByUsername", Member.class)
                    .setParameter("username", "member1")
                    .getResultList();


            // 벌크 연산 (한 번에 여러 값 수정)
            // 업데이트한 수 출력
            // 벌크 연산 수행시 flush() 자동 수행
            int resultCount = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate();

            // 영속성 컨텍스트를 초기화 해줘야 벌크 연산이 반영된 데이터를 가져옴
           em.clear();

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}