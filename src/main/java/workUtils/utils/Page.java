package workUtils.utils;

import java.util.List;

/**
 * @ClassName: Page
 * @Description: TODO(Page bean.)
 * @author juesu.chen
 * @date Jun 16, 2014 1:39:45 PM
 * @version 1.0
 */
public class Page {
    /**
     * @Fields pageSize : TODO(one page size.)
     */
    private int pageSize;

    /**
     * @Fields startIndex : TODO(start page.)
     */
    private int startIndex;

    /**
     * @Fields pageCount : TODO(total page count.)
     */
    private int pageCount;

    /**
     * @Fields totalCount : TODO(total count.)
     */
    private int totalCount;

    private int offset;

    public Page() {

    }

    public Page(int pageSize, int startIndex) {
        setPageSize(pageSize);
        setStartIndex(startIndex);
    }

    /**
     * @Fields result : TODO(query lsit.)
     */
    private List result;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
        this.offset = (startIndex - 1) * pageSize;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        int count = totalCount / pageSize;
        if (totalCount % pageSize > 0) {
            count++;
        }
        this.setPageCount(count);
    }

    public List getResult() {
        return result;
    }

    public void setResult(List result) {
        this.result = result;
    }

    public int getOffset() {
        return offset;
    }
}
