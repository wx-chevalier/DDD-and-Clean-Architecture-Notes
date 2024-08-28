package com.d4i.sample.movie.category;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;



import com.d4i.sample.movie.shared.SelfValidating;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Category extends SelfValidating<Category> implements Serializable{

	@Min(0)
	private Long id;

	@NotEmpty
	private String name;

	private Boolean available;

	
}
