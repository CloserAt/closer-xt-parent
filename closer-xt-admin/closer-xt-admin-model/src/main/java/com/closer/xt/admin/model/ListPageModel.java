package com.closer.xt.admin.model;

import lombok.Data;

import javax.swing.*;
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
        ListPageModel<T> listPageModel = new ListPageModel<T>();
        listPageModel.setList(new ArrayList<T>());
        listPageModel.setPage(1);
        listPageModel.setPageCount(1);
        return listPageModel;
    }

}
