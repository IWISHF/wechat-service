package com.merkle.wechat.common.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

@Entity(name = "tag_group_condition")
public class TagGroupCondition {
    @Transient
    public static String AND_TYPE = "and";
    @Transient
    public static String OR_TYPE = "or";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String type;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "tag_group_condition_tag_mapping", joinColumns = @JoinColumn(name = "tagGroupConditionId", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "tagId", referencedColumnName = "id"))
    @OrderBy("id asc")
    @Size(min = 1)
    private Set<Tag> tags = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

}
