package com.lenarsharipov.simplebank.service.search;

import com.lenarsharipov.simplebank.model.*;
import jakarta.persistence.criteria.*;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

@Builder
@Data
public class ClientSpecification implements Specification<Client> {
    private String phone;
    private String fullName;
    private String email;
    private LocalDate birthDate;

    @Override
    public Predicate toPredicate(@NonNull Root<Client> root,
                                 @NonNull CriteriaQuery<?> query,
                                 CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.conjunction();
        if (phone != null && !phone.isEmpty()) {
            Join<Client, Phone> phoneJoin = root.join("phones", JoinType.INNER);
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.equal(phoneJoin.get("number"), phone));
        }
        if (fullName != null && !fullName.isEmpty()) {
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.like(root.get("fullName"), fullName + "%"));
        }
        if (email != null && !email.isEmpty()) {
            Join<Client, Email> emailJoin = root.join("emails", JoinType.INNER);
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.equal(emailJoin.get("address"), email));
        }
        if (birthDate != null) {
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.greaterThan(root.get("birth_date"), birthDate));
        }
        return predicate;
    }
}