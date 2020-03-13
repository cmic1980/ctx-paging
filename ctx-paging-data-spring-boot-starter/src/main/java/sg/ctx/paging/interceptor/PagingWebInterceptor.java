package sg.ctx.paging.interceptor;

import com.github.pagehelper.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import sg.ctx.paging.PageHelperContainer;
import sg.ctx.paging.config.PagingConfiguration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yu.miao
 */
@ControllerAdvice
public class PagingWebInterceptor implements HandlerInterceptor, ResponseBodyAdvice {
    @Value("${ctx.paging.size:50}")
    private int pageSize;

    public PagingWebInterceptor() {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String pageNUmberString = request.getHeader(PagingConfiguration.PAGE_NUMBER_TAG);
        if (StringUtils.isNotEmpty(pageNUmberString)) {
            int pageNUmber = Integer.parseInt(pageNUmberString);
            String pageSizeString = request.getHeader(PagingConfiguration.PAGE_SIZE_TAG);
            int pageSize;
            if (StringUtils.isEmpty(pageSizeString)) {
                pageSize = this.pageSize;
            } else {
                pageSize = Integer.parseInt(pageSizeString);
            }

            Page page = new Page();
            page.setPageNum(pageNUmber);
            page.setPageSize(pageSize);
            PageHelperContainer.setPage(page);
        }else{
            PageHelperContainer.setPage(null);
        }
        return true;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        var pageResult = PageHelperContainer.getResult();
        if (pageResult != null) {
            response.getHeaders().add(PagingConfiguration.PAGE_NUMBER_TAG, String.valueOf(pageResult.getPageNum()));
            response.getHeaders().add(PagingConfiguration.PAGE_SIZE_TAG, String.valueOf(pageResult.getPageSize()));
            response.getHeaders().add(PagingConfiguration.PAGE_COUNT_TAG, String.valueOf(pageResult.getPages()));
            response.getHeaders().add(PagingConfiguration.TOTAL_TAG, String.valueOf(pageResult.getTotal()));
        }
        return body;
    }
}

