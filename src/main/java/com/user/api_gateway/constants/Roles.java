package com.user.api_gateway.constants;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum Roles {

    PUBLIC("ROLE_PUBLIC","Public"),
    ADMINISTRATOR("ROLE_ADMINISTRATOR","Administrator"),
    NEWS_ENTERPRICE("ROLE_NEWS_ENTERPRICE","News enterprice"),
    PUBLISHER("ROLE_PUBLISHER","Publisher"),
    JOURNALIST("ROLE_JOURNALIST","Journalist"),
    READER("ROLE_READERS","Reader"),
    PREMIUM("ROLE_PREMIUM","Premium")
    ;

    String code;
    String nameFormat;

    private Roles(String code, String nameFormat){
        this.code = code;
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
