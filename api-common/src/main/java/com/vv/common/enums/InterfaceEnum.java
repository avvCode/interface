package com.vv.common.enums;

import lombok.Getter;

@Getter
public enum InterfaceEnum {
    OFFLINE(0,"下线"),
    ONLINE(1,"上线");


    private int code;
    private String message;
    InterfaceEnum(int code,String message){
        this.code = code;
        this.message = message;
    }
}
