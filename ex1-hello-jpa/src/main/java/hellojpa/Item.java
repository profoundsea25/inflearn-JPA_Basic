package hellojpa;

import javax.persistence.*;

@Entity

// 1. Joined 전략 (정석 전략)
@Inheritance(strategy = InheritanceType.JOINED)

// 2. Single Table 전략 (성능이 제일 좋다.)
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// @DiscriminatorColumn 생략해도 기본적으로 생긴다.

// 3. 구현 클래스마다 테이블 전략 (각각 테이블이 중복된 칼럼을 허용함)
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
// Item 클래스를 abstract 로 만들어야 한다.
// Item 으로 쿼리를 날려서 조회를 할 때 큰 비효율이 발생

@DiscriminatorColumn // Item Table 에 DTYPE 열이 생긴다. 운영하는 입장에서는 왠만하면 넣어주면 좋다.
// 상속 클래스들에 @DiscriminatorValue(value = "설정할 DTYPE 값")을 넣게 되면 DTYPE 값을 바꿀 수 있다.
public class Item {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private int price;
}