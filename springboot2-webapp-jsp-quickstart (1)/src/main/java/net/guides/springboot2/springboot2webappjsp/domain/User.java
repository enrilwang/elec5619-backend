package net.guides.springboot2.springboot2webappjsp.domain;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User
{


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer user_id;
	private String name;
	private Integer user_favorite_user_favorite_id;
	private String password;
	private String email;
	private String registerTime;



	public User()
	{
	}

	public User(Integer id, String name)
	{
		this.user_id = id;
		this.name = name;
	}


	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public Integer getId()
	{
		return user_id;
	}

	public void setId(Integer id)
	{
		this.user_id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public Integer getUser_favorite_user_favorite_id() {
		return user_favorite_user_favorite_id;
	}

	public void setUser_favorite_user_favorite_id(Integer user_favorite_user_favorite_id) {
		this.user_favorite_user_favorite_id = user_favorite_user_favorite_id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
