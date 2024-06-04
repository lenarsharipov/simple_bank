package com.lenarsharipov.simplebank.service.search;

import com.lenarsharipov.simplebank.model.*;
import jakarta.persistence.criteria.*;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

@Builder
public record UserSpecification(String phone,
                                String fullName,
                                String email,
                                LocalDate birthDate) implements Specification<User> {

    @Override
    public Predicate toPredicate(@NonNull Root<User> root,
                                 @NonNull CriteriaQuery<?> query,
                                 CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.conjunction();
        if (phone != null && !phone.isEmpty()) {
            Join<User, Phone> phoneJoin = root.join(User_.PHONES, JoinType.INNER);
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.equal(phoneJoin.get(Phone_.NUMBER), phone));
        }
        if (fullName != null && !fullName.isEmpty()) {
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.like(root.get(User_.FULL_NAME), fullName + "%"));
        }
        if (email != null && !email.isEmpty()) {
            Join<User, Email> emailJoin = root.join(User_.EMAILS, JoinType.INNER);
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.equal(emailJoin.get(Email_.ADDRESS), email));
        }
        if (birthDate != null) {
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.greaterThan(root.get(User_.BIRTH_DATE), birthDate));
        }
        return predicate;
    }
}