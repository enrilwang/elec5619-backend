package net.guides.springboot2.springboot2webappjsp.domain;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SubscribeId implements Serializable {
    private static final long serialVersionUID = -516102931790501845L;
    @Column(name = "subscirbe_id", nullable = false)
    private Integer subscirbeId;
    @Column(name = "payments_serial_id", nullable = false)
    private Integer paymentsSerialId;
    @Column(name = "subscription_type_id", nullable = false)
    private Integer subscriptionTypeId;
    @Column(name = "creator_id", nullable = false)
    private Integer creatorId;
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Integer getSubscriptionTypeId() {
        return subscriptionTypeId;
    }

    public void setSubscriptionTypeId(Integer subscriptionTypeId) {
        this.subscriptionTypeId = subscriptionTypeId;
    }

    public Integer getPaymentsSerialId() {
        return paymentsSerialId;
    }

    public void setPaymentsSerialId(Integer paymentsSerialId) {
        this.paymentsSerialId = paymentsSerialId;
    }

    public Integer getSubscirbeId() {
        return subscirbeId;
    }

    public void setSubscirbeId(Integer subscirbeId) {
        this.subscirbeId = subscirbeId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(subscirbeId, creatorId, subscriptionTypeId, paymentsSerialId, userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SubscribeId entity = (SubscribeId) o;
        return Objects.equals(this.subscirbeId, entity.subscirbeId) &&
                Objects.equals(this.creatorId, entity.creatorId) &&
                Objects.equals(this.subscriptionTypeId, entity.subscriptionTypeId) &&
                Objects.equals(this.paymentsSerialId, entity.paymentsSerialId) &&
                Objects.equals(this.userId, entity.userId);
    }
}