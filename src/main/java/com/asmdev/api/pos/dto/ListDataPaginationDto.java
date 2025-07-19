package com.asmdev.api.pos.dto;

import java.io.Serializable;
import java.util.List;

public class ListDataPaginationDto implements Serializable {

    List<Object> data;
    private int totalElements;

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }
}
