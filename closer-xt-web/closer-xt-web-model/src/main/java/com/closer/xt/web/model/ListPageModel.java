package com.closer.xt.web.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ListPageModel<T> implements Serializable {
    private long pageCount;

    private int page;

    private int pageSize;

    private long size;

    private List<T> list;

    public ListPageModel<T> initNull() {
        ListPageModel<T> listModel = new ListPageModel<>();
        listModel.setList(new ArrayList<>());
        listModel.setPage(1);
        listModel.setPageCount(1);
        listModel.setSize(0);
        return listModel;
    }

}
