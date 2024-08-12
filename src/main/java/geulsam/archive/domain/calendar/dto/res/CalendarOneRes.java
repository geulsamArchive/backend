package geulsam.archive.domain.calendar.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import geulsam.archive.domain.calendar.entity.Calendar;
import geulsam.archive.domain.calendar.entity.Criticism;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter(AccessLevel.PROTECTED)
public class CalendarOneRes {

    @Schema(description = "calendar id", example = "123", type = "string")
    private int id;

    @Schema(description = "calendar 시작시간", example = "2024년 5월 6일 14시 30분 45초", type = "string")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 M월 d일 HH시 mm분 ss초")
    private LocalDateTime start;

    @Schema(description = "calendar 끝나는 시간", example = "2024년 5월 6일 14시 30분 45초", type = "string")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 M월 d일 HH시 mm분 ss초")
    private LocalDateTime end;

    @Schema(description = "calendar 제목", example = "소풍", type = "string")
    private String title;

    @Schema(description = "calendar 소개", example = "무슨 일정인가요", type = "string")
    private String introduce;

    @Schema(description = "calendar 진행장소", example = "G420", type = "string")
    private String locate;

    public CalendarOneRes(Criticism criticism) {
        this.id = criticism.getId();
        this.start = criticism.getStart();
        this.end = criticism.getEnd();
        this.title = criticism.getTitle();
        this.introduce = criticism.getIntroduce();
        this.locate = criticism.getLocate();
    }
}
