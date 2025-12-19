package com.company.test;

public class ProgramDTO {
    private String name;
    private String code;
    private int domain;
    private boolean limited;
    private CoursDTO[] composition;

    public ProgramDTO() {}

    public ProgramDTO(String name, String code, int domain, boolean limited, CoursDTO[] composition) {
        this.name = name;
        this.code = code;
        this.domain = domain;
        this.limited = limited;
        this.composition = composition;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public int getDomain() { return domain; }
    public void setDomain(int domain) { this.domain = domain; }

    public boolean isLimited() { return limited; }
    public void setLimited(boolean limited) { this.limited = limited; }

    public CoursDTO[] getComposition() { return composition; }
    public void setComposition(CoursDTO[] composition) { this.composition = composition; }
}
