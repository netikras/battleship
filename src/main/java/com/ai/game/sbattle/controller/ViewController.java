package com.ai.game.sbattle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by netikras on 17.5.17.
 */
@Controller
public class ViewController {


    @RequestMapping(value = {"/", "/play"}, method = RequestMethod.GET)
    public ModelAndView play() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("game");
        return modelAndView;
    }





}
