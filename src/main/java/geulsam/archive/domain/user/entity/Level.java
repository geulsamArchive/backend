package geulsam.archive.domain.user.entity;

/**유저 등급
 * 관리자: ADMIN - 모든 api 접근 가능
 * 일반 유저: NORMAL
 * 졸업생: GRADUATED - NORMAL 에서 글 작성, 합평회 참여만 제한
 * 기타: SUSPENDED
 */
public enum Level {
    ADMIN,NORMAL,GRADUATED,SUSPENDED
}
