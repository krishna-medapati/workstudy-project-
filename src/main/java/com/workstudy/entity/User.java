package com.workstudy.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role { admin, student }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public static UserBuilder builder() { return new UserBuilder(); }
    public static class UserBuilder {
        private String name, email, password;
        private Role role;
        public UserBuilder name(String n) { this.name = n; return this; }
        public UserBuilder email(String e) { this.email = e; return this; }
        public UserBuilder password(String p) { this.password = p; return this; }
        public UserBuilder role(Role r) { this.role = r; return this; }
        public User build() {
            User u = new User();
            u.name = name; u.email = email;
            u.password = password; u.role = role;
            return u;
        }
    }
}
