package com.ostapchuk.tt.hashcat.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import static javax.persistence.GenerationType.IDENTITY;

@Table(name = "hash")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hash {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "decrypted", nullable = false, unique = true)
    private String decrypted;

    @Column(name = "encrypted", unique = true)
    private String encrypted;

    @ManyToMany
    @JoinTable(name = "application_hash",
            joinColumns = {@JoinColumn(name = "hash_id")},
            inverseJoinColumns = {@JoinColumn(name = "application_id")}
    )
    private Set<Application> applications = new HashSet<>();

    public Hash addApplication(final Application application) {
        if (this.applications == null) {
            this.applications = new HashSet<>();
        }
        this.applications.add(application);
        if (application.getHashes() == null) {
            application.setHashes(new HashSet<>());
        }
        application.getHashes().add(this);
        return this;
    }
}
