package jt.world.daletou.controller;

import jt.world.daletou.dto.DaLeTouPromptDto;
import jt.world.daletou.service.IDaLeTouService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* <p>
* 大乐透 前端控制器
* </p>
*
* @author JT
* @since 2025-12-29
*/
@RestController
@RequestMapping("/daLeTou")
@RequiredArgsConstructor
public class DaLeTouController {

    private final IDaLeTouService iDaLeTouService;

    @PostMapping("/getDaLeTouHistory")
    public void getDaLeTouHistory() {
        iDaLeTouService.getDaLeTouHistory();
    }

    @PostMapping("/getLatestDaLeTou")
    public void getLatestDaLeTou() {
        iDaLeTouService.getLatestDaLeTou();
    }

    @PostMapping("/getDaLeTouPredict")
    public ResponseEntity<?> getDaLeTouPredict(@RequestBody DaLeTouPromptDto daLeTouPromptDto) {
        return ResponseEntity.ok(iDaLeTouService.getDaLeTouPredict(daLeTouPromptDto));
    }
}
