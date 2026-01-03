package jt.world.spider;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class PlayWrightSpiderTest {

    /**
     * PlayWright 入门
     */
    @Test
    public void testPlayWright() {
        // 驱动默认下载位置 C:\Users\25472\AppData\Local\ms-playwright
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
            Page page = browser.newPage();
            page.navigate("https://playwright.dev");
            System.out.println(page.title());
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("example.png")));
        }
    }

    /**
     * PlayWright 断言、定位器、选择器
     * 断言 assertThat 的一些重载函数会等待，知道满足预期条件
     * 定位器 Locator，PlayWright支持多种定位器
     * BrowserContext 内存中独立的浏览器配置文件，建议每个测试创建新的BrowserContext，确保其之间相互独立
     */
    @Test
    public void testPlayWrightAssert() {
        // 创建Playwright实例
        try (Playwright playwright = Playwright.create()) {
            // 启动 chromium 浏览器并返回一个浏览器实例
            Browser browser = playwright.chromium().launch();
            // 创建一个新的浏览器上下文
            BrowserContext context = browser.newContext();
            // 创建一个新的页面
            Page page = context.newPage();
            // 导航到指定URL
            page.navigate("https://playwright.dev");

            // 断言页面标题是Playwright
            assertThat(page).hasTitle(Pattern.compile("Playwright"));

            // 创建一个定位器，其中，Aria（Accessible Rich Internet Applications）可访问的富互联网应用
            Locator getStarted = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Get Started"));

            // 断言定位器中的元素有 href 属性，并且值为 /docs/intro
            assertThat(getStarted).hasAttribute("href", "/docs/intro");

            // 点击“开始”链接。
            getStarted.click();

            // 断言页面标题是“安装”
            assertThat(page.getByRole(AriaRole.HEADING,
                    new Page.GetByRoleOptions().setName("Installation"))).isVisible();
        }
    }

    /**
     * PlayWright 录制
     */
    @Test
    public void testPlayWrightRecord() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            BrowserContext context = browser.newContext(/* pass any options */);
            context.route("**/*", route -> route.resume());
            Page page = context.newPage();
            // 暂停页面，等待用户操作，触发Playwright的调试模式
            page.pause();
        }
    }

    /**
     * PlayWright 记录轨迹
     */
    @Test
    public void testPlayWrightRecordTrail() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            BrowserContext context = browser.newContext(/* pass any options */);

            // 开始记录
            context.tracing().start(new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
                    .setSources(true));

            Page page = context.newPage();
            page.navigate("https://playwright.dev");

            // 停止记录并导出记录文件
            context.tracing().stop(new Tracing.StopOptions()
                    .setPath(Paths.get("trace.zip")));
        }
    }
}
