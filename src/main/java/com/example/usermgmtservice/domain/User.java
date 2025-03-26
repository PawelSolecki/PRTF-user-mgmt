package com.example.usermgmtservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("users")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User implements Persistable<UUID> {

    @Id
    private UUID id;
    private String name;
    private String email;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    @Transient
    private boolean isNew;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }
}
