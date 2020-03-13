package sg.ctx.paging.interceptor;

import com.github.pagehelper.Page;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sg.ctx.paging.config.PagingConfiguration;

import java.io.IOException;

/**
 * @author yu.miao
 */
public class FeignResponseInterceptor implements Interceptor {

    public static final ThreadLocal<Page> LOCAL_PAGE = new ThreadLocal<Page>();

    @Override
    public Response intercept(Chain chain) throws IOException {
        //第一步，获得chain内的request
        Request serviceRequest = chain.request();

        //第二步，用chain执行request
        Response serviceResponse = chain.proceed(serviceRequest);

        var pageNumberString = serviceResponse.header(PagingConfiguration.PAGE_NUMBER_TAG);
        var pageSizeString = serviceResponse.header(PagingConfiguration.PAGE_SIZE_TAG);
        var pageCountString = serviceResponse.header(PagingConfiguration.PAGE_COUNT_TAG);

        if(StringUtils.isNotEmpty(pageNumberString) && StringUtils.isNotEmpty(pageNumberString) && StringUtils.isNotEmpty(pageCountString))
        {
            var pageNumber = Integer.parseInt(pageNumberString);
            var pageSize = Integer.parseInt(pageSizeString);
            var pageCount = Integer.parseInt(pageCountString);

            var page = new Page();
            page.setPageNum(pageNumber);
            page.setPageSize(pageSize);
            page.setPages(pageCount);
            LOCAL_PAGE.set(page);
        }

        //第三步，返回response
        return serviceResponse;
    }
}
