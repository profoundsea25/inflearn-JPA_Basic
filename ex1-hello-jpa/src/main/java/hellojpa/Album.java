package hellojpa;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
// @DiscriminatorValue(value = "A") // DTYPE 을 사용할 경우 값을 설정할 수 있다.
public class Album extends Item {
    private String artist;
}
