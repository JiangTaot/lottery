package ${package.Controller};

import ${package.Service}.${table.serviceName};
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* <p>
* ${table.comment!} 前端控制器
* </p>
*
* @author ${author}
* @since ${date}
*/
@RestController
@RequestMapping("/${table.entityPath}")
@RequiredArgsConstructor
public class ${table.controllerName} {

    private final ${table.serviceName} ${table.serviceName?uncap_first};

}