package jt.world.ai;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jt.world.daletou.entity.DaLeTou;
import jt.world.daletou.service.IDaLeTouService;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("acer")
public class DaLeTouAiPredictTest {

    @Autowired
    private ChatModel chatModel;
    @Autowired
    private IDaLeTouService daLeTouService;

    @Test
    public void testPredictDaLeTou() {
        List<DaLeTou> daLeTouHistory = daLeTouService.list(new LambdaQueryWrapper<DaLeTou>().orderBy(true, true, DaLeTou::getId));

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

        String prompt = "根据以下历史大乐透开奖数据,第25124期开奖号码是什么" + trainingText +
                "\n请输出格式为：红球：[x,x,x,x,x]，蓝球：[x,x]";

        ChatClient chatClient = ChatClient.builder(chatModel).build();

        ChatResponse chatResponse = chatClient.prompt().user(prompt).call().chatResponse();

        System.out.println(chatResponse.getResult().getOutput().getText());
        UserMessage userMessage = new UserMessage(prompt);
        List<Message> messages = List.of(userMessage);

        prompt = "第25125期开奖号码是什么";
        ChatResponse chatResponse2 = chatClient.prompt().messages(messages).user(prompt).call().chatResponse();
        System.out.println(chatResponse2.getResult().getOutput().getText());
    }

}
