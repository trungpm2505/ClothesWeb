package com.web.clothes.ClothesWeb.controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.clothes.ClothesWeb.dto.mapper.Mapper;
import com.web.clothes.ClothesWeb.dto.requestDto.AttributeValueRequestDto;
import com.web.clothes.ClothesWeb.dto.responseDto.AttributeValueResponseDto;
import com.web.clothes.ClothesWeb.dto.responseDto.AttributeValuePageResponseDto;
import com.web.clothes.ClothesWeb.entity.Attribute;
import com.web.clothes.ClothesWeb.entity.AttributeValue;
import com.web.clothes.ClothesWeb.service.AttributeService;
import com.web.clothes.ClothesWeb.service.AttributeValueService;
import com.web.clothes.ClothesWeb.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/attributeValue")
public class AttributeValueController {

	private final AttributeValueService attributeValueService;
	private final AttributeService attributeService;
	private final UserService userService;
	private final Mapper mapper;
	private static Log log = LogFactory.getLog(AttributeValueController.class);
	
	// add attribute value
	@PostMapping(value = "/add")
	@ResponseBody
	public ResponseEntity<?> addAttributeValue(@Valid @RequestBody AttributeValueRequestDto attributeValueRequestDto,
			BindingResult bindingResult, Model model) {
		log.info("Add attribute value with name "+ attributeValueRequestDto.getAttributeValueName());
		
		Map<String, Object> errors = new HashMap<>();

		// validate input data
		if (bindingResult.hasErrors()) {
			log.error("Add attribute value with name "+attributeValueRequestDto.getAttributeValueName()+" have error "+bindingResult.getAllErrors() );
			errors.put("bindingErrors", bindingResult.getAllErrors());
		}

		// check if attribute is exist
		Optional<Attribute> attribute = attributeService.getAttribute(attributeValueRequestDto.getAttributeName());
		// check if Attribute value name is exist
		Optional<AttributeValue> attributeValueByName = attributeValueService
				.findAttributeValueByName(attributeValueRequestDto.getAttributeValueName().trim());
		if (attribute.isEmpty()) {
			log.error("Add attribute value with name "+attributeValueRequestDto.getAttributeValueName()+" has 'error not found attribute'");
			errors.put("NotFoundAttribute", "The system is having problems! Please try again later");

		}
		if (attributeValueByName.isPresent() ) {
			log.error("Add attribute value with name "+attributeValueRequestDto.getAttributeValueName()+" has error 'name already exists'");
			errors.put("attributeExist", "Attribute value already exists! Please enter a new one!");
		}

		if (!errors.isEmpty()) {
			return ResponseEntity.badRequest().body(errors);
		}

		// map attributeValueRequestDto to attributeValue
		AttributeValue attributeValue = mapper.attributeValueRequestDtoToAttributeValue(attributeValueRequestDto);
		attributeValueService.save(attributeValue);
		
		String success = "A new attribute added successfully.";
		log.info("Add attribute value with name "+attributeValueRequestDto.getAttributeValueName()+"  successfully." );
		return ResponseEntity.ok().body(success);
	}

	@PutMapping(value = "/update")
	@ResponseBody
	public ResponseEntity<?> updateAttributeValue(@RequestParam Integer attributeValueId,
			@Valid @RequestBody AttributeValueRequestDto attributeValueRequestDto, BindingResult bindingResult) {

		log.info("Update attribute value with id "+attributeValueId);
		Map<String, Object> errors = new HashMap<>();
		// validate input data
		if (bindingResult.hasErrors()) {
			
			log.error("Update attribute value with id "+attributeValueId+" have error "+bindingResult.getAllErrors() );

			errors.put("bindingErrors", bindingResult.getAllErrors());
			return ResponseEntity.badRequest().body(errors);
		}

		// check if attribute is exist
		Optional<Attribute> attribute = attributeService.getAttribute(attributeValueRequestDto.getAttributeName());
		if (attribute.isEmpty()) {
			log.error("Update attribute value with id "+attributeValueId+" have error 'attribute not exist'" );
			
			errors.put("error", "The system is having problems! Please try again later");
			return ResponseEntity.badRequest().body(errors);

		}

		// check if Attribute value is exist
		Optional<AttributeValue> attributeValueById = attributeValueService.getAttributeValue(attributeValueId);
		Optional<AttributeValue> attributeValueByName = attributeValueService
				.findAttributeValueByName(attributeValueRequestDto.getAttributeValueName().trim());
		
		if (attributeValueById.isEmpty()) {
			log.error("Update attribute value with id "+attributeValueId+" have error 'attribute value not exist'" );
			
			errors.put("error", "Attribute value is not exist! Update failse!");
			return ResponseEntity.badRequest().body(errors);

		} else if (attributeValueByName.isPresent() && !(attributeValueByName.get().getId()).equals(attributeValueId)) {
			// check if Attribute value name is exist
			log.error("Update attribute value with id "+attributeValueId+" have error ' name already exist'" );
			
			errors.put("error", "Attribute value name already exists! Please enter a new one!");
			return ResponseEntity.badRequest().body(errors);
		}
		// map attributeValueRequestDto to attributeValue
		AttributeValue attributeValue = mapper.attributeValueRequestDtoToAttributeValue(attributeValueRequestDto);
		attributeValue.setId(attributeValueId);
		attributeValueService.save(attributeValue);
		
		log.info("Update attribute value with id "+attributeValueId+"  successfully." );
		return ResponseEntity.ok().body("A attribute updated successfully.");
	}

	@DeleteMapping(value = "/delete")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> deleteAttributeValue(@RequestParam("attributeValueId") Integer attributeValueId) {
		log.info("Delete attribute value with id "+attributeValueId);
		
		// check if Attribute value is exist
		Optional<AttributeValue> attributeValueById = attributeValueService.getAttributeValue(attributeValueId);
		if (attributeValueById.isEmpty() || attributeValueById.get().getDeleteAt()!=null) {
			
			log.error("Delete attribute value with id "+attributeValueId+" have error 'attribute value not exist'" );
			
			return ResponseEntity.badRequest().body("Attribute value is not exist! Delete failse!");
		}
		
		attributeValueService.deleteAttributeValue(attributeValueById.get());
		
		log.info("Deleted attribute value with id "+attributeValueId+"  successfully." );
		return ResponseEntity.ok().body("A attribute deleted successfully.");
	}
	
	//return view attribute value
	@GetMapping()
	public String getAttributeView(Model model) {
		log.info("get view attribute value." );
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("userName", userService.findUserByEmail(authentication.getName()).get().getUserName());
		return "admin/attribute";
	}
	
	//get page attribute value
	@GetMapping(value = "/getAttributePage")
	@ResponseBody
	public ResponseEntity<AttributeValuePageResponseDto> getAttributePage(
			@RequestParam(defaultValue = "8") int size, @RequestParam(defaultValue = "0") int page,
			@RequestParam String attributeName,@RequestParam(required = false) String key) {
		log.info("get attribute value page value." );
		
		AttributeValuePageResponseDto attributeValuePageResponseDto = attributeValueService.getAttributeValueByAttribute(page, size, attributeName,key);

		return ResponseEntity.ok(attributeValuePageResponseDto);

	}
	
	@GetMapping(value = "/getAll")
	@ResponseBody
	public ResponseEntity<List<AttributeValueResponseDto>> getAttributeList(@RequestParam String attributeName) {
		log.info("get all attribute value." );
		List<AttributeValue> attributeValues = attributeValueService.getList(attributeName);
		List<AttributeValueResponseDto> attributeValueResponseDto = attributeValues.stream()
				.map(attributeValue -> new AttributeValueResponseDto(attributeValue.getId(),
						attributeValue.getAttributeValueName()))
				.collect(Collectors.toList());
		return ResponseEntity.ok(attributeValueResponseDto);

	}
	
	

}
