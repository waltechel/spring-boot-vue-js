package com.taskagile.infrastructure.repository;

import javax.persistence.EntityManager;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.taskagile.domain.model.user.User;
import com.taskagile.domain.model.user.UserRepository;

@Repository
public class HibernateUserRepository extends HibernateSupport implements UserRepository {

  public HibernateUserRepository(EntityManager entityManager) {
    super(entityManager);
  }

  @Override
  public User findByUsername(String username) {
    Query<User> query = getSession().createQuery("from User where username = :username", User.class);
    query.setParameter("username", username);
    return query.uniqueResult();
  }

  @Override
  public User findByEmailAddress(String emailAddress) {
    Query<User> query = getSession().createQuery("from User where emailAddress = :emailAddress", User.class);
    query.setParameter("emailAddress", emailAddress);
    return query.uniqueResult();
  }

  @Override
  public void save(User user) {
    entityManager.persist(user);
    entityManager.flush();
  }
}