package by.backer.dao.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {
    /* DB id */
    protected Long id;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }
}
