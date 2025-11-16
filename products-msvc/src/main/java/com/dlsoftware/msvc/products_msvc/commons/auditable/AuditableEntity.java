package com.dlsoftware.msvc.products_msvc.commons.auditable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EntityListeners(value= {AuditingEntityListener.class})
@MappedSuperclass
public class AuditableEntity implements Serializable {

    @JsonIgnore
    @Column(updatable = false)
    @CreatedBy
    private String createdBy;

    @JsonIgnore
    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @JsonIgnore
    @Column(insertable =  false)
    @LastModifiedBy
    private String lastModifiedBy;

    @JsonIgnore
    @Column(insertable =  false)
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}