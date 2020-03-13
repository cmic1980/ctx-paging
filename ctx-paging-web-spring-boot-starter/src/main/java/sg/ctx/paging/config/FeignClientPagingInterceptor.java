package sg.ctx.paging.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import sg.ctx.paging.interceptor.FeignResponseInterceptor;


/**
 * @author yumiao
 */
@Configuration
@ControllerAdvice
public class FeignClientPagingInterceptor implements RequestInterceptor, ResponseBodyAdvice {

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        var request = attributes.getRequest();

        var pageNUmberString = request.getHeader(PagingConfiguration.PAGE_NUMBER_TAG);
        if (StringUtils.isNotEmpty(pageNUmberString)) {
            template.header(PagingConfiguration.PAGE_NUMBER_TAG, pageNUmberString);
        }

        var pageSizeString = request.getHeader(PagingConfiguration.PAGE_SIZE_TAG);
        if (StringUtils.isNotEmpty(pageSizeString)) {
            template.header(PagingConfiguration.PAGE_SIZE_TAG, pageSizeString);
        }
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        var page = FeignResponseInterceptor.LOCAL_PAGE.get();
        if (page != null) {
            response.getHeaders().add(PagingConfiguration.PAGE_NUMBER_TAG, String.valueOf(page.getPageNum()));
            response.getHeaders().add(PagingConfiguration.PAGE_SIZE_TAG, String.valueOf(page.getPageSize()));
            response.getHeaders().add(PagingConfiguration.PAGE_COUNT_TAG, String.valueOf(page.getPages()));
        }
        return body;
    }
}
