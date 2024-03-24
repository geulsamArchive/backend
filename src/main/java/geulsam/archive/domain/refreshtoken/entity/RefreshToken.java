package geulsam.archive.domain.refreshtoken.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class RefreshToken {

    @Id
    @Column(name = "refreshToken_id")
    private Integer id;

    @Column(name = "refreshToken_value")
    private String token;

    public void changeTokenValue(String token){
        this.token = token;
    }
}
