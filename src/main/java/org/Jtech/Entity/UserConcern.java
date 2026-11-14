package org.Jtech.Entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.Jtech.Constant.Concern;

@Entity
@Table(
        name = "user_concern",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_user_concern",
                columnNames = {"details_id", "concern"}
        )
)
public class UserConcern extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "details_id", nullable = false)
    private UserDetails userDetails;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "concern", nullable = false)
    private Concern concern;
    public UserConcern() {
    }

    public UserConcern(UserDetails userDetails, Concern concern) {
        this.userDetails = userDetails;
        this.concern = concern;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public Concern getConcern() {
        return concern;
    }

    public void setConcern(Concern concern) {
        this.concern = concern;
    }
}
