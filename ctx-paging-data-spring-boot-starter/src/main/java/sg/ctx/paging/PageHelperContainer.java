package sg.ctx.paging;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * @author yu.miao
 */
public class PageHelperContainer {
    protected static final ThreadLocal<Page> LOCAL_PAGE = new ThreadLocal<Page>();
    protected static final ThreadLocal<PagingResult> PAGING_RESULT = new ThreadLocal<PagingResult>();

    public static void setPage(Page page) {
        LOCAL_PAGE.set(page);
    }

    public static void start() {
        var page = LOCAL_PAGE.get();
        if (page != null) {
            PageHelper.startPage(page.getPageNum(), page.getPageSize());
        }
    }

    public static void end(PagingResult result) {
        PAGING_RESULT.set(result);
        PageHelper.clearPage();
    }

    public static PagingResult getResult() {
       var result = PAGING_RESULT.get();
       return result;
    }
}
