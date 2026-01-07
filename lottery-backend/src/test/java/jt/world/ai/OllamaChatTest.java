package jt.world.ai;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jt.world.daletou.entity.DaLeTou;
import jt.world.daletou.service.IDaLeTouService;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
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
        List<String> daLeTouNumbers = new ArrayList<>();
        List<String> daLeTouIds = new ArrayList<>();
        for (DaLeTou record : daLeTouHistory) {
            trainingText.append("期号: ").append(record.getId())
                    .append(", 开奖时间: ").append(record.getDrawTime())
                    .append(", 红球: ").append(record.getRedOne()).append(",").append(record.getRedTwo())
                    .append(",").append(record.getRedThree()).append(",").append(record.getRedFour())
                    .append(",").append(record.getRedFive())
                    .append(", 蓝球: ").append(record.getBlueOne()).append(",").append(record.getBlueTwo())
                    .append("\n");
            StringBuilder stringBuilder = new StringBuilder();
            daLeTouNumbers.add(stringBuilder.append("期号: ").append(record.getId())
                    .append(", 开奖时间: ").append(record.getDrawTime())
                    .append(", 红球: ").append(record.getRedOne()).append(",").append(record.getRedTwo())
                    .append(",").append(record.getRedThree()).append(",").append(record.getRedFour())
                    .append(",").append(record.getRedFive())
                    .append(", 蓝球: ").append(record.getBlueOne()).append(",").append(record.getBlueTwo())
                    .append("\n").toString());
            daLeTouIds.add(record.getId());
        }
        List<Message> messages = new ArrayList<>();
        String prompt = "某博彩规则为红色球从01-35中取5个不一样的号，蓝色球从01-12中取2个不一样的号";
        System.out.println(prompt);
        messages.add(UserMessage.builder().text(prompt).build());

        ChatResponse chatResponse1 = ollamaChatClient.prompt().messages(messages).user(prompt).call().chatResponse();
        String response1 = chatResponse1.getResult().getOutput().getText();
        AssistantMessage assistantMessage1 = AssistantMessage.builder().content(response1).build();
        messages.add(assistantMessage1);

        prompt = "根据上述规则，通过我喂给你的数据，开始预测下一期的数据，并通过我提供的后续数据，开始拟合，生成一个模型，不断的优化这个模型，不用输出这个模型，知道预测的下一期数据和我提供的下一期数据完全相同的时候，保存该模型";
        System.out.println(prompt);
        messages.add(UserMessage.builder().text(prompt).build());

        ChatResponse chatResponse2 = ollamaChatClient.prompt().messages(messages).user(prompt).call().chatResponse();
        String response2 = chatResponse2.getResult().getOutput().getText();
        AssistantMessage assistantMessage2 = AssistantMessage.builder().content(response2).build();
        messages.add(assistantMessage2);

        for (int i = 0; i < daLeTouIds.size(); i++) {
            prompt = "现在我提供第" + daLeTouIds.get(i) + "期数据" + daLeTouNumbers.get(i) + "，预测一下" + daLeTouIds.get(i + 1) + "期的数据";
            System.out.println(prompt);
            messages.add(UserMessage.builder().text(prompt).build());
            ChatResponse chatResponse = ollamaChatClient.prompt().messages(messages).user(prompt).call().chatResponse();
            String response = chatResponse.getResult().getOutput().getText();
            System.out.println(response);
            AssistantMessage assistantMessage = AssistantMessage.builder().content(response).build();
            messages.add(assistantMessage);
        }


        System.out.println("--------------------");
    }
}
