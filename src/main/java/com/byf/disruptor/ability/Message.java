package com.byf.disruptor.ability;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Message implements Serializable {

	private static final long serialVersionUID = 2035546038986494352L;
	private Long id ;
	private String name;
	
	public Message() {
	}
}
