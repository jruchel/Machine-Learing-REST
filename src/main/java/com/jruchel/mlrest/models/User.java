package com.jruchel.mlrest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jruchel.mlrest.validation.user.EmailConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
public class User implements UserDetails {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", unique = true)
    @NotNull
    private String username;
    @Column(name = "password")
    @NotNull
    private String password;
    @Column(name = "email")
    @EmailConstraint
    private String email;
    @JsonIgnore
    private UUID secret;
    private boolean locked;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Model> models;

    public User() {
        this.roles = new HashSet<>();
        this.models = new HashSet<>();
        secret = UUID.randomUUID();
    }

    public void setRoles(Set<Role> roles) {
        this.roles = new HashSet<>();
        for (Role r : roles) {
            if (r != null) this.roles.add(r);
        }
    }

    public void setModels(Set<Model> models) {
        this.models = new HashSet<>();
        for (Model m : models) {
            if (m != null) this.models.add(m);
        }
    }

    public void addModel(Model model) {
        if (model == null) return;
        this.models.add(model);
        model.setOwner(this);
    }

    public void grantRole(Role role) {
        if (role == null) return;
        this.roles.add(role);
        role.addUser(this);
    }

    public void revokeRole(Role role) {
        if (role == null) return;
        this.roles.removeIf(r -> r.getTitle().equals(role.getTitle()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
