package net.guides.springboot2.springboot2webappjsp.domain;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;

@Table(name = "user")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public User(){}

    @Column(name = "username", nullable = false, length = 45)
    private String username;

    @Column(name = "password", nullable = false, length = 200)
    private String password;

    @Column(name = "register_time", nullable = false)
    private String registerTime;

    @Column(name = "description",  length = 1024)
    private String description;

    @Column(name = "email", length = 45)
    private String email;

    @Column(name = "account_balance", length = 45)
    private float accountBalance;

    @Column(name = "profile_pic_store")
    private String profilePicStore;

    @Column(name = "is_admin")
    private String isAdmin;

    @Column(name = "is_creator")
    private String isCreator;

    @Column(name = "favorite_id", length = 1024)
    private String favoriteId;

    @Column(name = "subscribe_id", length = 1024)
    private String subscribeId;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "artifact_id")
    private Set<Artifact> artifacts;



    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public Integer getId() {
        return id;
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

    public String getIsCreator() {
        return isCreator;
    }

    public void setIsCreator(String isCreator) {
        this.isCreator = isCreator;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getProfilePicStore() {
        return profilePicStore;
    }

    public void setProfilePicStore(String profilePicStore) {
        this.profilePicStore = profilePicStore;
    }

    public float getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(float accountBalance) {
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

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
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

}
