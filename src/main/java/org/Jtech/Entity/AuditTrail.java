package org.Jtech.Entity;

import jakarta.persistence.*;
import org.Jtech.Constant.AuditAction;

import java.time.LocalDateTime;

@Entity
@Table(name="audit_trail")
public class AuditTrail {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auditId;

    @Column(name = "operation_id",updatable = false,nullable = false)
    private String operationId;

    @Column(name="entity_name",nullable = false)
    private String entityName;

    @Column(name="entity_id",nullable = false)
    private Long entityId;

    @Column(name="action",updatable = false,nullable = false)
    @Enumerated(EnumType.STRING)
    private AuditAction action;

    @Column(name="changed_field" )
    private String changedField;

    @Column(name="previous_value")
    private String previousValue;

    @Column(name="current_value")
    private String currentValue;

    @Column(name ="performed_by",nullable = false,updatable = false)
    private Long performedBy;

    @Column(name="performed_at",nullable = false, updatable = false)
    private LocalDateTime performedAt;

    @Column(name="request_id")
    private String requestId;

    @Column(name="ip_address")
    private String ipAddress;

    public Long getAuditId() {
        return auditId;
    }

    public void setAuditId(Long auditId) {
        this.auditId = auditId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public Long getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(Long performedBy) {
        this.performedBy = performedBy;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public AuditAction getAction() {
        return action;
    }

    public void setAction(AuditAction action) {
        this.action = action;
    }

    public String getChangedField() {
        return changedField;
    }

    public void setChangedField(String changedField) {
        this.changedField = changedField;
    }

    public String getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(String previousValue) {
        this.previousValue = previousValue;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public LocalDateTime getPerformedAt() {
        return performedAt;
    }

    public void setPerformedAt(LocalDateTime performedAt) {
        this.performedAt = performedAt;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
