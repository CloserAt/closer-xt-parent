package com.closer.xt.web.model.enums;

import java.util.HashMap;
import java.util.Map;

public enum TabEnum {
    TK(0, "题库"),
    SX(1,"升学"),
    OTHER(2,"其他");

    private static final Map<Integer, TabEnum> CODE_MAP = new HashMap<>(3);

    static{
        for(TabEnum tabEnum : values()){
            CODE_MAP.put(tabEnum.getCode(), tabEnum);
        }
    }

    /**
     * 根据code获取枚举值
     * @param code
     * @return
     */
    public static TabEnum valueOfCode(int code){
        return CODE_MAP.get(code);
    }

    private int code;
    private String msg;

    TabEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
