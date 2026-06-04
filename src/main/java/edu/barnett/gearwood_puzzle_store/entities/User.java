package edu.barnett.gearwood_puzzle_store.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    // Requirements don't indicate multiple roles, but previous examples have. Leaving this option available
    private final Set<Role> roles = new HashSet<>();

    // A user's cart is modeled as CartItem rows that reference this user (see CartItem.user).
    // Cart logic lives in CartService, which queries/mutates those rows via CartItemRepository,
    // so there is intentionally no cart collection mapped here.

    @OneToMany(mappedBy="user", cascade = CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval = true)
    private final List<OrderData> orderData = new ArrayList<>();

    public User() {}

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Set<Role> getRoles() { return roles; }
    public void addRole(Role role) {
        this.roles.add(role);
    }
    public void setRoles(Set<Role> roles) {
        this.roles.addAll(roles);
    }

}
