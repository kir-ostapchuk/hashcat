package com.ostapchuk.tt.hashcat.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Table(name = "client")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "link_uuid", nullable = false, unique = true)
    private String linkUuid;

    @OneToMany(fetch = LAZY, mappedBy = "client")
    private Set<Application> applications;
//
//    public User addApplication(final Application application) {
//        if (this.applications == null) {
//            this.applications = Set.of(application);
//        } else {
//            this.applications.add(application);
//        }
//        return this;
//    }
}
