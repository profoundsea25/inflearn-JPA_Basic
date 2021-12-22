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
//
//            // 조회하기
//            Member findMember = em.find(Member.class, 1L);
//
//            // 수정하기
//            Member findMember = em.find(Member.class, 1L);
//            findMember.setName("HelloJPA");
//
//            // 조회하기 : 직접 쿼리를 작성하는 방법
//            List<Member> result = em.createQuery("select m from Member as m", Member.class)
//                            .getResultList();
//
//            // 비영속 상태
//            Member member = new Member();
//            member.setId(100L);
//            member.setName("HelloJPA");
//
//            // 영속
//            // 영속 시점에 DB로 쿼리가 날아가지 않는다.
//            em.persist(member);
//
////            // 영속성 컨텍스트에서 분리, 준영속 상태 (영속성 컨텍스트 기능 사용 불가)
////            em.detach(member);
//              // 영속성 컨텍스트 완전 비움
//              em.clear();
//              // 영속성 컨텍스트 종료
//              em.close();
//
////            // 삭제 (DB에서 삭제)
////            em.remove(member);
//
//            // 영속성 컨텍스트에 의해 DB에 쿼리가 안날아가고 1차 캐시에서 조회
//            Member findMember = em.find(Member.class, 100L);
//            Member findMember2 = em.find(Member.class, 100L);
//            // findMember == findMember2
//            // >>> true (by 영속성 컨텍스트)
//            
//            
//            // 쓰기 지연 확인
//            Member member1 = new Member(150L, "A");
//            Member member2 = new Member(160L, "B");
//
//            em.persist(member1);
//            em.persist(member2);
            
//            // Dirty Checking by 영속성 컨텍스트
//            Member member = em.find(Member.class, 150L);
//            member.setName("AAA"); // 다른 코드 필요없이 commit 하면 내용이 변경된다. (Dirty Checking)
//            // commit 이 호출되면 em.flush()가 실행되어 update 쿼리를 만든다.
//            // 강제로 commit 보다 변경 감지를 일찍 실행하고 싶으면 em.flush()를 실행하면 된다.
//            // 쓰기 지연 : commit 시점에 DB로 쿼리가 날아간다.
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}