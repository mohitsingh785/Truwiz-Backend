package org.Jtech.DTO;

import org.Jtech.Model.HarmfulChemicalTable;

import java.util.List;

public class HarmfulChemicalResponseDTO {
    private List<HarmfulChemicalTable> harmfulChemicalTableList;

    public List<HarmfulChemicalTable> getHarmfulChemicalTableList() {
        return harmfulChemicalTableList;
    }

    public void setHarmfulChemicalTableList(List<HarmfulChemicalTable> harmfulChemicalTableList) {
        this.harmfulChemicalTableList = harmfulChemicalTableList;
    }
}
