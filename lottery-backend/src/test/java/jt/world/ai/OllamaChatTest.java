package jt.world.ai;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jt.world.daletou.entity.DaLeTou;
import jt.world.daletou.service.IDaLeTouService;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("acer")
public class OllamaChatTest {
    @Autowired
    @Qualifier("ollamaChatClient")
    private ChatClient ollamaChatClient;
    @Autowired
    private IDaLeTouService daLeTouService;


    @Test
    public void testOllamaChat() {

        List<DaLeTou> daLeTouHistory = daLeTouService.list(new LambdaQueryWrapper<DaLeTou>().orderBy(true, true, DaLeTou::getId));
        daLeTouHistory.remove(daLeTouHistory.size() - 1);
        StringBuilder trainingText = new StringBuilder();
        for (DaLeTou record : daLeTouHistory) {
            trainingText.append("期号: ").append(record.getId())
                    .append(", 开奖时间: ").append(record.getDrawTime())
                    .append(", 红球: ").append(record.getRedOne()).append(",").append(record.getRedTwo())
                    .append(",").append(record.getRedThree()).append(",").append(record.getRedFour())
                    .append(",").append(record.getRedFive())
                    .append(", 蓝球: ").append(record.getBlueOne()).append(",").append(record.getBlueTwo())
                    .append("\n");
        }

        String prompt = trainingText +
                "根据以上提供的历史数据,合理分析,第26001期号码是什么" +
                "\n请输出格式为：红球：[x,x,x,x,x]，蓝球：[x,x]" +
                "\n加上分析规则，比如说，存在规则，不存在同样的号，比如说，红球5连号的概率特别小，等等";

        ChatResponse chatResponse = ollamaChatClient.prompt().user(prompt).call().chatResponse();
        System.out.println(chatResponse.getResult().getOutput().getText());
    }
}
