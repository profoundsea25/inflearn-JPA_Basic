package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
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
//            // 저장하기
//            Member member = new Member();
//            member.setId(1L);
//            member.setName("HelloA");
//            em.persist(member);

//            // 조회하기
//            Member findMember = em.find(Member.class, 1L);

            // 수정하기
            Member findMember = em.find(Member.class, 1L);
            findMember.setName("HelloJPA");

            // 조회하기 : 직접 쿼리를 작성하는 방법
            List<Member> result = em.createQuery("select m from Member as m", Member.class)
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