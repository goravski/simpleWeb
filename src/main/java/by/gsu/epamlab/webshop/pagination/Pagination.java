package by.gsu.epamlab.webshop.pagination;


public class Pagination {
    private int maxRows;
    protected int currentPage;
    private  int offset;

    public Pagination() {
        maxRows = 5;
        currentPage = 1;
        offset = countOffsetPass(maxRows,currentPage);
    }

    public Pagination(int currentPage) {
        maxRows = 5;
        this.currentPage = currentPage;
        offset = countOffsetPass(maxRows,currentPage);
    }

    public int getOffset() {
        return offset;
    }


    public int getMaxRows() {
        return maxRows;
    }


    public int getCurrentPage() {
        return currentPage;
    }

    public static int countOffsetPass(int rowsPerPage, int currentPage) {
        return (currentPage-1)*rowsPerPage;
    }


}
