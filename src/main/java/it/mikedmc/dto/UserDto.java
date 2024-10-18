package it.mikedmc.dto;

public class UserDto {
	private String username;
	private String password;
	private String password2;
	private String email;
	private String code;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword2() {
		return password2;
	}
	public void setPassword2(String password2) {
		this.password2 = password2;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public UserDto pulisciPassword() {
		this.password=null;
		this.password2=null;
		return this;
	}
	public UserDto pulisciUsername() {
		this.username=null;
		return this;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
