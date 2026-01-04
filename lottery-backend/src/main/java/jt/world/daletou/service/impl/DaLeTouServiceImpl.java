package jt.world.daletou.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jt.world.daletou.dto.DaLeTouPromptDto;
import jt.world.daletou.entity.DaLeTou;
import jt.world.daletou.mapper.DaLeTouMapper;
import jt.world.daletou.service.IDaLeTouService;
import jt.world.spider.DaLeTouSpider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
@Slf4j
public class DaLeTouServiceImpl extends ServiceImpl<DaLeTouMapper, DaLeTou> implements IDaLeTouService {

    private final DaLeTouSpider daLeTouSpider;
    private final ChatClient chatClient;

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

    @Override
    public String getDaLeTouPredict(DaLeTouPromptDto daLeTouPromptDto) {
        // 大乐透历史数据
        StringBuilder trainingText = new StringBuilder();
        if (Boolean.TRUE.equals(daLeTouPromptDto.getIsDaLeTouHistory())) {
            List<DaLeTou> daLeTouHistory = this.list(new LambdaQueryWrapper<DaLeTou>().orderBy(true, true, DaLeTou::getId));
            for (DaLeTou record : daLeTouHistory) {
                trainingText.append("期号: ").append(record.getId())
                        .append(", 开奖时间: ").append(record.getDrawTime())
                        .append(", 红球: ").append(record.getRedOne()).append(",").append(record.getRedTwo())
                        .append(",").append(record.getRedThree()).append(",").append(record.getRedFour())
                        .append(",").append(record.getRedFive())
                        .append(", 蓝球: ").append(record.getBlueOne()).append(",").append(record.getBlueTwo())
                        .append("\n");
            }
        }
        // 提示词
        String prompt = daLeTouPromptDto.getIsDaLeTouHistory() ? "根据以下历史大乐透开奖数据，" + trainingText + daLeTouPromptDto.getPrompt() : daLeTouPromptDto.getPrompt();

        ChatResponse chatResponse = chatClient.prompt().user(prompt).call().chatResponse();
        assert chatResponse != null;
        String response = chatResponse.getResult().getOutput().getText();
        log.info(response);
        return response;

    }
}
