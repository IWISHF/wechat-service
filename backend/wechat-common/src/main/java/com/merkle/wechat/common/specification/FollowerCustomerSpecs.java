package com.merkle.wechat.common.specification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.merkle.wechat.common.entity.Tag;
import com.merkle.wechat.common.entity.TagGroupCondition;
import com.merkle.wechat.common.entity.follower.Follower;

public class FollowerCustomerSpecs {
    public static Specification<Follower> isSubscribe(Integer subscribe) {
        return new Specification<Follower>() {
            @Override
            public Predicate toPredicate(Root<Follower> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (subscribe == -1) {
                    return cb.greaterThan(root.get("subscribe"), subscribe);
                }

                return cb.equal(root.get("subscribe"), subscribe);
            }
        };
    }

    /**
     * subscribeTime between start and end time
     * 
     * @param start
     *            seconds
     * @param end
     *            seconds
     * @return
     */
    public static Specification<Follower> subscribeTimeBetween(Long start, Long end) {
        return new Specification<Follower>() {
            @Override
            public Predicate toPredicate(Root<Follower> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.between(root.get("subscribeTime"), start, end);
            }
        };
    }

    public static Specification<Follower> tagsContaining(Set<Tag> tags) {
        return new Specification<Follower>() {

            @Override
            public Predicate toPredicate(Root<Follower> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                final Collection<Predicate> predicates = new ArrayList<>();

                for (Tag tag : tags) {
                    predicates.add(cb.isMember(tag, root.get("tags")));

                }

                for (Tag tag : tags) {
                    predicates.add(cb.isMember(tag, root.get("tags")));

                }

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

    public static Specification<Follower> tagGroupCondition(Set<TagGroupCondition> groups) {
        return new Specification<Follower>() {

            @Override
            public Predicate toPredicate(Root<Follower> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Collection<Predicate> allPredicates = new ArrayList<>();
                for (TagGroupCondition group : groups) {
                    Collection<Predicate> predicates = new ArrayList<>();
                    Set<Tag> tags = group.getTags();

                    for (Tag tag : tags) {
                        predicates.add(cb.isMember(tag, root.get("tags")));

                    }

                    if (group.getType().equals(TagGroupCondition.AND_TYPE)) {
                        allPredicates.add(cb.and(predicates.toArray(new Predicate[predicates.size()])));
                    } else {
                        allPredicates.add(cb.or(predicates.toArray(new Predicate[predicates.size()])));
                    }

                }

                return cb.or(allPredicates.toArray(new Predicate[allPredicates.size()]));
            }
        };
    }

    public static Specification<Follower> sexIs(int sex) {
        return new Specification<Follower>() {
            @Override
            public Predicate toPredicate(Root<Follower> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (sex == -1) {
                    return cb.greaterThan(root.get("sex"), sex);
                }
                return cb.equal(root.get("sex"), sex);
            }
        };
    }

    public static Specification<Follower> pbNoIdIs(String pubNoAppId) {
        return new Specification<Follower>() {

            @Override
            public Predicate toPredicate(Root<Follower> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get("pubNoAppId"), pubNoAppId);
            }
        };
    }
}
