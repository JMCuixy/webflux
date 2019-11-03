package org.spring.webflux.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : cuixiuyin
 * @date : 2019/11/3
 */
@Component
public class CustomWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        if (queryParams == null || StringUtils.isEmpty(queryParams.getFirst("token"))) {
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("code", "401");
            resultMap.put("msg", "非法请求");
            byte[] datas = new byte[0];
            try {
                datas = new ObjectMapper().writeValueAsBytes(resultMap);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            ServerHttpResponse response = exchange.getResponse();
            DataBuffer buffer = response.bufferFactory().wrap(datas);
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            return response.writeWith(Mono.just(buffer));
        }

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();
            //Manipulate the response in some way
        }));
    }
}
