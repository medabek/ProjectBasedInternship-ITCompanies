package com.ourFinalProject.turizm.service;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;

import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;

public class LimitController implements Pageable {
    private int limit;
    private int offset;
    private SpringDataWebProperties.Sort sort;
    public LimitController(int offset, int limit){
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    public int getNumberOfPages() {
        return 0;
    }

    @Override
    public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
        return null;
    }

    @Override
    public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
        return null;
    }

}