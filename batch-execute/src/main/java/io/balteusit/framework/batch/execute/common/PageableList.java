package io.balteusit.framework.batch.execute.common;

import java.util.List;

public class PageableList<T> {

  private int page;

  private int size;

  private long total;

  private List<T> content;

  public PageableList(int page, int size, long total, List<T> content) {
    this.page = page;
    this.size = size;
    this.total = total;
    this.content = content;
  }

  public int getPage() {
    return page;
  }

  public int getSize() {
    return size;
  }

  public long getTotal() {
    return total;
  }

  public List<T> getContent() {
    return content;
  }

}
