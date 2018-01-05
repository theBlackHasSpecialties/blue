package com.hujiang.blue.controller;


import com.hujiang.blue.service.SpellCheckService;
import com.hujiang.blue.vo.SpellCheckVo;
import com.hujiang.java.common.utility.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = {"/v1/blue"})
@CrossOrigin(origins = "*")
public class SpellCheckController extends BaseAppController {

    @Autowired
    private SpellCheckService spellCheckService;

    @RequestMapping(value = "/word", method = RequestMethod.POST)
    public Result<List<String>> wordSpellCheck(@RequestBody String words) throws Exception {

        String[] word = words.split(" ");
        if(word.length > 0)
            return buildResult(spellCheckService.wordSpellCheck(word[0]));
        else
            return buildResult(null);
    }

    @RequestMapping(value = "/sentence", method = RequestMethod.POST)
    public Result<List<SpellCheckVo>> getSpellError(@RequestBody String sentence) throws Exception {
        return buildResult(spellCheckService.getSpellError(sentence));
    }
}
