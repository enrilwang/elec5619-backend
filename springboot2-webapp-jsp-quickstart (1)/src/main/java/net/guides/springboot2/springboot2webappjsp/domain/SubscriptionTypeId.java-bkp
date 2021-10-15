package net.guides.springboot2.springboot2webappjsp.domain;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SubscriptionTypeId implements Serializable {
    private static final long serialVersionUID = -8888885444320315270L;
    @Column(name = "subscription_type_id", nullable = false)
    private Integer subscriptionTypeId;
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSubscriptionTypeId() {
        return subscriptionTypeId;
    }

    public void setSubscriptionTypeId(Integer subscriptionTypeId) {
        this.subscriptionTypeId = subscriptionTypeId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(subscriptionTypeId, userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SubscriptionTypeId entity = (SubscriptionTypeId) o;
        return Objects.equals(this.subscriptionTypeId, entity.subscriptionTypeId) &&
                Objects.equals(this.userId, entity.userId);
    }
}
