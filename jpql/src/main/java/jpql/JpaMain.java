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

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

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

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}