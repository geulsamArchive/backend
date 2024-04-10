package geulsam.archive.global.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ErrorCode ENUM: 발생 가능한 에러들을 한곳에 정리
 */
@AllArgsConstructor
@Getter
public enum ErrorCode {
    VALUE_ERROR(400, "VALUE_ERROR", "값이 유효하지 않습니다."),
    PAGE_ERROR(400,"PAGE_ERROR","페이지 범위 에러"),
    LOGIN_ERROR(403,"LOGIN_ERROR", "로그인 시간 지남"),
    AUTH_ERROR(403, "AUTH_ERROR","인증 실패");


    private final int status;
    private final String error;
    private final String message;
}
