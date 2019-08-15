package org.spring.webflux.route;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : cuixiuyin
 * @date : 2019/8/14
 */
@RestController
public class IndexController {

    @GetMapping("/index")
    public String index() {
        return "index";
    }
}
