package jt.world.daletou.service;

import jt.world.daletou.dto.DaLeTouPromptDto;
import jt.world.daletou.entity.DaLeTou;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 大乐透 服务类
 * </p>
 *
 * @author JT
 * @since 2025-12-29
 */
public interface IDaLeTouService extends IService<DaLeTou> {

    void getDaLeTouHistory();

    void getLatestDaLeTou();

    String getDaLeTouPredict(DaLeTouPromptDto daLeTouPromptDto);
}
