package com.hujiang.blue.vo;

import lombok.Data;

import java.util.List;

@Data
public class SpellCheckVo {
    private int fromPos;
    private int toPos;
    private String shortMessage;
    private List<String> suggestedReplacements;
    private String message;

    public SpellCheckVo(int fromPos, int toPos, String shortMessage, List<String> suggestedReplacements, String message) {
        this.fromPos = fromPos;
        this.toPos = toPos;
        this.shortMessage = shortMessage;
        this.suggestedReplacements = suggestedReplacements;
        this.message = message;
    }
}
