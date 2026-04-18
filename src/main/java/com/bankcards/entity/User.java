package com.bankcards.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;

    @Email
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true; // account active/inactive

    // Many-to-many User<->Role via join table 'user_roles'
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false)
    )
    private Set<Role> roles = new HashSet<>();

    // One-to-many User -> Card, mapped by 'owner' field in Card
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Card> cards = new ArrayList<>();

    /* Constructors */

    public User() {
        // no-arg constructor
    }

    public User(String username, String password, String email, boolean enabled) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.enabled = enabled;
    }

    /* Getters & Setters */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Card>  getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    /* helper */

    public void addRole(Role role) {
        if (role == null) return;
        this.roles.add(role);
        if (role.getUsers() != null && !role.getUsers().contains(this)) {
            role.getUsers().add(this);
        }
    }

    public void removeRole(Role role) {
        if (role == null) return;
        this.roles.remove(role);
        if (role.getUsers() != null) {
            role.getUsers().remove(this);
        }
    }

    public void addCard(Card card) {
        if (card == null) return;
        if (!this.cards.contains(card)) {
            this.cards.add(card);
        }

        if (card.getOwner() != this) {
            card.setOwner(this);
        }
    }

    public void removeCard(Card card) {
        if (card == null) return;
        this.cards.remove(card);
        if (card.getOwner() == this) {
         card.setOwner(null);
        }
    }
}