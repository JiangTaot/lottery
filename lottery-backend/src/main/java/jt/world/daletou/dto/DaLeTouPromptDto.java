package jt.world.daletou.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DaLeTouPromptDto {
    /**
     * 提示
     */
    private String prompt;
    /**
     * 是否使用大乐透历史数据
     */
    private Boolean isDaLeTouHistory;
}
