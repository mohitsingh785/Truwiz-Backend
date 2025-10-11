package org.Jtech.DTO;

import org.Jtech.Model.GoodChemicalTable;

import java.util.List;

public class GoodChemicalResponseDTO {

    private List<GoodChemicalTable> goodChemicalTableList;

    public List<GoodChemicalTable> getGoodChemicalTableList() {
        return goodChemicalTableList;
    }

    public void setGoodChemicalTableList(List<GoodChemicalTable> goodChemicalTableList) {
        this.goodChemicalTableList = goodChemicalTableList;
    }
}
