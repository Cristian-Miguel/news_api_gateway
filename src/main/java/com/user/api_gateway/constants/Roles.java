package com.user.api_gateway.constants;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum Roles {

    PUBLIC("Public"),
    ADMINISTRATOR("Administrator"),
    NEWS_ENTERPRICE("News enterprice"),
    PUBLISHER("Publisher"),
    JOURNALIST("Journalist"),
    READER("Reader"),
    PREMIUM("Premium")
    ;

    String nameFormat;

    private Roles(String nameFormat){
        this.nameFormat = nameFormat;
    }

    public static Roles matchRoleByNameFormat(String nameFormat){
        return Arrays.stream(Roles.values())
                .filter(roleEnum -> roleEnum.getNameFormat().equals(nameFormat))
                .findFirst()
                .orElse(null);
    }

    public static Roles matchRoleByName(String name){
        return Arrays.stream(Roles.values())
                .filter(roleEnum -> roleEnum.name().equals(name))
                .findFirst()
                .orElse(null);
    }

}
