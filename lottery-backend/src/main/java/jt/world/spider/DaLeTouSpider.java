package jt.world.spider;

import com.microsoft.playwright.*;
import com.microsoft.playwright.junit.UsePlaywright;
import jt.world.daletou.entity.DaLeTou;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@UsePlaywright
@Slf4j
public class DaLeTouSpider {

    private static final String DA_LE_TOU_HISTORY_URL = "https://www.lottery.gov.cn/kj/kjlb.html?dlt";

    /**
     * 获取大乐透历史数据
     *
     * @return List<DaLeTou> 大乐透历史数据
     */
    public List<DaLeTou> getDaLeTouHistory() {
        log.info("开始获取大乐透历史数据...");
        try (Playwright playwright = Playwright.create()) {
            // 启动 chromium 浏览器并返回一个浏览器实例
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            // 创建一个新的浏览器上下文
            BrowserContext context = browser.newContext();
            // 创建一个新的页面
            Page page = context.newPage();
            // 访问大乐透页面
            page.navigate(DA_LE_TOU_HISTORY_URL);
            // 大乐透数据
            List<DaLeTou> results = new ArrayList<>();
            // 获取iframe标签
            FrameLocator iframe = page.locator("iframe[name=\"iFrame1\"]").contentFrame();
            // 进入网站后获取第一页的大乐透数据，之后循环点击下一页，获取下一页数据，直到没有下一页或下一页点击不了时停止循环
            boolean hasNextPage = true;
            int count = 0;
            while (hasNextPage) {
                // 经分析获取其<tbody id="historyData">标签中的所有tr标签
                List<ElementHandle> trs = iframe.locator("tbody#historyData tr").elementHandles();

                /**
                 * 获取tr标签中的第一行td标签中的数据为期号，
                 * td标签中class="lineb2Rt"的数据为开奖日期，
                 * 第3行到第7行td标签中class="u-dltpre"的数据为红色开奖号码
                 * 第8行td标签中class="u-dltnext"中span标签包裹的第一个数据为第一个蓝色开奖号码
                 * 第9行td标签中class="u-dltnext lineb2Rt"中s数据为第二个蓝色开奖号码
                 */
                for (ElementHandle tr : trs) {
                    // 提取期号
                    ElementHandle tdIssue = tr.querySelector("td");
                    // 跳过派奖等干扰行（含 colspan="2" 或 class 包含 bgecf5fe）
                    if (tdIssue != null &&
                            (tdIssue.getAttribute("colspan") != null ||
                                    (tdIssue.getAttribute("class") != null && tdIssue.getAttribute("class").contains("bgecf5fe"))
                            )
                    ) {
                        continue;
                    }

                    String issue = tdIssue.textContent().trim();

                    // 提取开奖时间
                    ElementHandle tdDate = tr.querySelector("td.lineb2Rt");
                    String date = tdDate.textContent().trim();

                    // 提取红色球（第3~7个td，class=u-dltpre）
                    List<String> redBalls = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        ElementHandle redBallTd = tr.querySelector("td.u-dltpre:nth-child(" + (i + 3) + ")");
                        if (redBallTd != null) {
                            redBalls.add(redBallTd.textContent().trim());
                        }
                    }

                    // 提取蓝色球1（第8个td，class=u-dltnext）
                    ElementHandle blueBall1Td = tr.querySelector("td.u-dltnext:nth-child(8)");
                    String blueBall1 = blueBall1Td != null ? blueBall1Td.textContent().trim() : "";

                    // 提取蓝色球2（第9个td，class=u-dltnext lineb2Rt）
                    ElementHandle blueBall2Td = tr.querySelector("td.u-dltnext.lineb2Rt:nth-child(9)");
                    String blueBall2 = blueBall2Td != null ? blueBall2Td.textContent().trim() : "";

                    // 构造 DaLeTou 对象并加入结果列表
                    DaLeTou daLeTou = new DaLeTou();
                    daLeTou.setId(issue);
                    daLeTou.setDrawTime(LocalDateTime.of(LocalDate.parse(date), LocalTime.of(21, 30, 0)));
                    daLeTou.setRedOne(redBalls.get(0));
                    daLeTou.setRedTwo(redBalls.get(1));
                    daLeTou.setRedThree(redBalls.get(2));
                    daLeTou.setRedFour(redBalls.get(3));
                    daLeTou.setRedFive(redBalls.get(4));
                    daLeTou.setBlueOne(blueBall1);
                    daLeTou.setBlueTwo(blueBall2);

                    results.add(daLeTou);
                }

                // 到达最后一页的时候，下一页是灰色的，如何判断
                Locator nextPageButton = iframe.getByText("下一页");
                String nextPageButtonAttr = nextPageButton.getAttribute("class");
                if (nextPageButtonAttr != null && nextPageButtonAttr.contains("no")) {
                    count ++;
                    if (count >= 2) {
                        hasNextPage = false;
                    } else {
                        nextPageButton.click();
                    }
                } else {
                    nextPageButton.click();
                }
            }
            log.info("获取大乐透历史数据完成，共获取{}条数据", results.size());
            return results;
        } catch (Exception e) {
            log.error("获取大乐透历史数据失败", e);
            return null;
        }
    }
}
