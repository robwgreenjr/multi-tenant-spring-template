package template.global.advice;

import template.database.models.QueryResult;
import template.global.models.MetaData;
import template.global.models.QueryResponse;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Order(2)
@ControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        if (body instanceof QueryResult) {
            return new QueryResponse<>(((QueryResult<?>) body).getData(),
                    ((ServletServerHttpResponse) response).getServletResponse(),
                    ((QueryResult<?>) body).getMeta());
        }

        return new QueryResponse<>(body,
                ((ServletServerHttpResponse) response).getServletResponse(),
                new MetaData());
    }
}
