package net.guides.springboot2.springboot2webappjsp.domain;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "subscription_type")
@Entity
public class SubscriptionType {
    @EmbeddedId
    private SubscriptionTypeId id;

    @Column(name = "tier_0", nullable = false, length = 45)
    private String tier0;

    @Column(name = "tier_1", length = 45)
    private String tier1;

    @Column(name = "tier_2", length = 45)
    private String tier2;

    public SubscriptionType() {

    }

    public String getTier2() {
        return tier2;
    }

    public void setTier2(String tier2) {
        this.tier2 = tier2;
    }

    public String getTier1() {
        return tier1;
    }

    public void setTier1(String tier1) {
        this.tier1 = tier1;
    }

    public String getTier0() {
        return tier0;
    }

    public void setTier0(String tier0) {
        this.tier0 = tier0;
    }

    public SubscriptionTypeId getId() {
        return id;
    }

    public void setId(SubscriptionTypeId id) {
        this.id = id;
    }
}
