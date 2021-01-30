package com.fscloud.lib_base.view.pinyin;

import java.io.IOException;

/**
 * 自定义异常
 * @author Administrator
 *
 */
public class PinyinException extends IOException {
    public PinyinException(String message){
        super(message);
    }
}
