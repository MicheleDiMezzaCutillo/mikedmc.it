package it.mikedmc.dto.builder;

import it.mikedmc.dto.UserDto;
import it.mikedmc.model.User;

public class UserDtoBuilder {
	
	public static User fromDtoToEntity (UserDto userDto) {
		User user = new User();
		user.setUsername(userDto.getUsername());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		return user;
	}
	
}
