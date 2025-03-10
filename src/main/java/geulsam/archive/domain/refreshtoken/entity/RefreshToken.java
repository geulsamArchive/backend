package geulsam.archive.domain.refreshtoken.entity;

import geulsam.archive.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "refreshToken_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "refreshToken_value")
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public void changeTokenValue(String token){
        this.token = token;
    }

    public RefreshToken(String token, User user){
        this.token = token;
        this.user = user;
    }
}
