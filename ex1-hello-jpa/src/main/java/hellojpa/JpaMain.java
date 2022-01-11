package hellojpa;

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
            // 임베디드 타입
//            Address address = new Address("city", "street", "10000");
//
//            MemberForEmbeddedType member = new MemberForEmbeddedType();
//            member.setUsername("member1");
//            member.setHomeAddress(address);
//            em.persist(member);
//
//            // 수정하고 싶으면, 새로운 객체를 생성해서 갈아끼워야 한다.
//            // 그래야 부작용을 막을 수 있다.
//            Address newAddress = new Address("newCity", address.getStreet(), address.getZipcode())
//            member.setHomeAddress(newAddress);

            // 컬렉션 값 타입
            MemberForCollectionType member = new MemberForCollectionType();
            member.setUsername("member1");
            member.setHomeAddress(new Address("homeCity", "street", "10000"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("피자");
            member.getFavoriteFoods().add("족발");

//            member.getAddressHistory().add(new Address("oldCity1", "street", "10000"));
//            member.getAddressHistory().add(new Address("oldCity2", "street", "10000"));
            member.getAddressHistory().add(new AddressEntity("oldCity1", "street", "10000"));
            member.getAddressHistory().add(new AddressEntity("oldCity2", "street", "10000"));
            // member만 persist 해도 같이 저장 된다. embedded와 마찬가지로.
            em.persist(member);

            em.flush();
            em.clear();

            MemberForCollectionType findMember = em.find(MemberForCollectionType.class, member.getId());

//            findMember.getHomeAddress().setCity("newCity"); // 이런식으로 하면 안 된다. immutable하게 해야 한다!
            // 새로운 인스턴스로 통으로 갈아끼우자.
            Address a = findMember.getHomeAddress();
            findMember.setHomeAddress(new Address("newCity", a.getStreet(), a.getZipcode()));

            // 치킨을 한식으로 바꾸고 싶다면?
            findMember.getFavoriteFoods().remove("치킨");
            findMember.getFavoriteFoods().add("한식");

            // 주소를 바꾸는 법 (equals와 hashCode 메소드를 Override 해놓아야 remove가 제대로 작동한다)
//            findMember.getAddressHistory().remove(new Address("oldCity1", "street", "10000"));
//            findMember.getAddressHistory().add(new Address("newCity1", "street", "10000"));
            // 이렇게 하면 쿼리가 필요 이상으로 나간다.
            // 컬렉션을 통째로 날리기 때문!
            // 따라서 AddressEntity 같은 Entity를 따로 만들어 관리하자.
            findMember.getAddressHistory().remove(new AddressEntity("oldCity1", "street", "10000"));
            findMember.getAddressHistory().add(new AddressEntity("newCity1", "street", "10000"));


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}