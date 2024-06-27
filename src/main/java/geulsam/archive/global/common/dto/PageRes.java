package geulsam.archive.global.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PageRes<T> {
    /**총 페이지 수*/
    @Schema(example = "3")
    private int pageTotal;
    private List<T> content;
}
