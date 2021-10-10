package net.guides.springboot2.springboot2webappjsp.domain;

import javax.persistence.*;
import java.time.Instant;

@Table(name = "user")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Column(name = "username", nullable = false, length = 45)
    private String username;

    @Column(name = "password", nullable = false, length = 16)
    private String password;

    @Column(name = "register_time", nullable = false)
    private Instant registerTime;

    @Column(name = "description", nullable = false, length = 1024)
    private String description;

    @Column(name = "email", length = 45)
    private String email;

    @Column(name = "account_balance", length = 45)
    private String accountBalance;

    @Column(name = "profile_pic_store")
    private String profilePicStore;

    @Column(name = "is_admin", nullable = false)
    private Integer isAdmin;

    @Column(name = "is_creator", nullable = false)
    private Integer isCreator;

    @Column(name = "favorite_id", length = 1024)
    private String favoriteId;

    @Column(name = "subscribe_id", length = 1024)
    private String subscribeId;

    @Column(name = "id", nullable = false)
    private Integer id1;

    @Column(name = "name")
    private String name;

    public User() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId1() {
        return id1;
    }

    public void setId1(Integer id1) {
        this.id1 = id1;
    }

    public String getSubscribeId() {
        return subscribeId;
    }

    public void setSubscribeId(String subscribeId) {
        this.subscribeId = subscribeId;
    }

    public String getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(String favoriteId) {
        this.favoriteId = favoriteId;
    }

    public Integer getIsCreator() {
        return isCreator;
    }

    public void setIsCreator(Integer isCreator) {
        this.isCreator = isCreator;
    }

    public Integer getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getProfilePicStore() {
        return profilePicStore;
    }

    public void setProfilePicStore(String profilePicStore) {
        this.profilePicStore = profilePicStore;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Instant registerTime) {
        this.registerTime = registerTime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}