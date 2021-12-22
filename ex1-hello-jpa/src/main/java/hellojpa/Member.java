package hellojpa;

import javax.persistence.*;
import java.util.Date;

@Entity // 꼭 넣어야 JPA라고 인식함. DB 테이블과 매핑
// Table(name = "MBR") // 이렇게 쓰면 이 클래스를 테이블로 만들 때 MBR로 인식
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String username;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    // 최신 버전에선 어노테이션 없이 LocalDate 쓰면 된다.
    // private LocalDate createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;
    // 최신 버전에선 어노테이션 없이 LocalDateTime 쓰면 된다.
    // private LocalDateTime lastModifiedDate;

    // 필드 매핑을 안 하고 싶으면 @Transient 를 쓰자자

   @Lob // varchar 이상의 큰 데이터를 쓰고 싶으면 @Lob 활용
    private String description;

    public Member() {
    }
}