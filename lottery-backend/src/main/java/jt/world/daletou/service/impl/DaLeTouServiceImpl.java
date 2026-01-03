package jt.world.daletou.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jt.world.daletou.entity.DaLeTou;
import jt.world.daletou.mapper.DaLeTouMapper;
import jt.world.daletou.service.IDaLeTouService;
import jt.world.spider.DaLeTouSpider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 大乐透 服务实现类
 * </p>
 *
 * @author JT
 * @since 2025-12-29
 */
@Service
@RequiredArgsConstructor
public class DaLeTouServiceImpl extends ServiceImpl<DaLeTouMapper, DaLeTou> implements IDaLeTouService {

    private final DaLeTouSpider daLeTouSpider;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void getDaLeTouHistory() {
        List<DaLeTou> daLeTouHistory = daLeTouSpider.getDaLeTouHistory();
        if (CollectionUtil.isEmpty(daLeTouHistory)) {
            return;
        }
        boolean isSaved = this.saveBatch(daLeTouHistory);
        if (!isSaved) {
            throw new RuntimeException("保存大乐透数据失败");
        }
    }
}
