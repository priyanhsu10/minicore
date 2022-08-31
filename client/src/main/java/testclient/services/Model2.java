package testclient.services;

import minicore.contracts.annotations.modelBinding.FromHeader;

public class Model2 {
    public Model2() {
    }

    public String getName() {
        return name;
    }

    private String Language;
    @FromHeader(key="Acce")
    private String Accept;

}
