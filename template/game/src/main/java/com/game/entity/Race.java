package com.game.entity;

public enum Race {
    HUMAN("human"),
    DWARF("dwarf"),
    ELF("elf"),
    GIANT("giant"),
    ORC("orc"),
    TROLL("troll"),
    HOBBIT("HOBBIT");

    private final String fieldName;

    Race(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}