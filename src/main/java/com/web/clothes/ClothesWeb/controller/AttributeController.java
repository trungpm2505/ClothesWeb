package com.web.clothes.ClothesWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.web.clothes.ClothesWeb.service.AttributeService;

@Controller
public class AttributeController {
	@Autowired
	private AttributeService attributeService;
}
