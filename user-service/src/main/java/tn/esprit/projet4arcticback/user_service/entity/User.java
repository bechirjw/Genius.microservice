package tn.esprit.projet4arcticback.user_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class User implements Serializable, UserDetails, Principal {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long idUser;
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;
    private String nom;
    private String prenom;
    @Email(message = "Email should be valid")
    @Column(unique = true, nullable = false)
    //@ValidEmailDomain(domain = "esprit.tn")
    private String email;
    private String motDePasse;


    @Pattern(regexp = "^\\+(?:[0-9] ?){6,14}[0-9]$", message = "Phone number must be in valid international format")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role roles;

    private boolean accountLocked;
    private boolean enabled;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime LastModifiedDate;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String image;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(roles.name()));
    }

    @Override
    public String getPassword() {
        return motDePasse;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String fullName() {
        return nom + " " + prenom;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }


    // constructeur(s)…

    @Override
    public String getName() {
        return email;
    }


}