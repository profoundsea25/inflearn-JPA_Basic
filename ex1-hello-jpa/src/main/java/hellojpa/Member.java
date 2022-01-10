package hellojpa;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity // 꼭 넣어야 JPA라고 인식함. DB 테이블과 매핑
// Table(name = "MBR") // 이렇게 쓰면 이 클래스를 테이블로 만들 때 MBR로 인식
public class Member extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

//    @Column(name = "TEAM_ID")
//    private Long teamId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID")
    private Team team;
    // 다:1 관계를 가지는 쪽은 주인으로 지정해라.
    // 일단 쿼리가 나가는 것에 대해 헷갈리지 않을 수 있으며, 성능 우위가 있다.

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;

//    @ManyToMany
//    @JoinTable(name = "MEMBER_PRODUCT")
//    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberProduct> memberProducts = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

    //    public Long getTeamId() {
//        return teamId;
//    }
//
//    public void setTeamId(Long teamId) {
//        this.teamId = teamId;
//    }

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//
//    @Column(name = "name")
//    private String username;
//
//    private Integer age;
//
//    @Enumerated(EnumType.STRING)
//    private RoleType roleType;
//
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date createdDate;
//    // 최신 버전에선 어노테이션 없이 LocalDate 쓰면 된다.
//    // private LocalDate createdDate;
//
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date lastModifiedDate;
//    // 최신 버전에선 어노테이션 없이 LocalDateTime 쓰면 된다.
//    // private LocalDateTime lastModifiedDate;
//
//    // 필드 매핑을 안 하고 싶으면 @Transient 를 쓰자자
//
//   @Lob // varchar 이상의 큰 데이터를 쓰고 싶으면 @Lob 활용
//    private String description;
//
//    public Member() {
//    }
}