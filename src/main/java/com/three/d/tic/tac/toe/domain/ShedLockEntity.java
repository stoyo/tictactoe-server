package com.three.d.tic.tac.toe.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "shedlock")
public class ShedLockEntity {

    @Id
    @Column(length = 64)
    private String name;

    @Column(name = "lock_until", columnDefinition = "timestamp(3)")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date lockUntil;

    @Column(name = "locked_at", columnDefinition = "timestamp(3)")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date lockedAt;

    @Column(name = "locked_by", nullable = false)
    private String lockedBy;
}
