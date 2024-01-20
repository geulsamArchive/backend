package geulsam.archive.domain.content.entity;

/**콘텐츠 공개 여부
 * PRIVATE: 작성자만 열람 가능
 * LOGGEDIN: 로그인 유저만 열람 가능
 * EVERY: 비로그인 유저도 열람 가능
 */
public enum IsVisible {
    PRIVATE,LOGGEDIN,EVERY
}
