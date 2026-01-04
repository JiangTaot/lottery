package jt.world.ai;

import jt.world.daletou.entity.DaLeTou;
import jt.world.daletou.service.IDaLeTouService;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class DaLeTouAiPredictTest {

    @Autowired
    private ChatModel chatModel;
    @Autowired
    private IDaLeTouService daLeTouService;

    @Test
    public void testPredictDaLeTou() {
        List<DaLeTou> daLeTouHistory = daLeTouService.list();

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

//        String prompt = "根据以下历史大乐透开奖数据，预测下一期的红球和蓝球号码：" + trainingText +
//                "\n请输出格式为：红球：[x,x,x,x,x]，蓝球：[x,x]";

        String prompt = "根据以下历史大乐透开奖数据，红球：[07, 09, 23, 27, 32]，蓝球：[02, 08] 出现过吗？有出现过类似的号码吗，在什么时候，类似号码是什么" + trainingText +
                "\n请输出格式为：红球：[x,x,x,x,x]，蓝球：[x,x]，这注号码作为25150期开奖号码概率大吗";



        ChatClient chatClient = ChatClient.builder(chatModel).build();

        ChatResponse chatResponse = chatClient.prompt(prompt).call().chatResponse();

        System.out.println(chatResponse.getResult().getOutput().getText());
    }
}
