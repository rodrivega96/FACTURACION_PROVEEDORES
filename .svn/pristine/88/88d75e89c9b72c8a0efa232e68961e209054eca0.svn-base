package com.vates.eng.ha.persistence;

import static org.testng.AssertJUnit.assertEquals;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.vates.eng.ha.persistence.domain.User;
import com.vates.eng.ha.persistence.service.UserService;

/**
 * @author Gaston Napoli
 *
 */
@ContextConfiguration(classes = { com.vates.eng.ha.persistence.TestPersistenceConfig.class })
@ActiveProfiles("ha.db.test")
public class UserServiceTest extends AbstractTransactionalTestNGSpringContextTests {

    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    private UserService userService;

    @Test
    @Transactional
    public void testSaveAndFindRepository() throws Exception {

        User user = new User();

        user.setActive(true);
        
        user.setFirstName("Juan");
        
        user.setLastName("Perez");
        
        user.setLogin("jperez");
        
        entityManager.persist(user);

        entityManager.flush();

        // Otherwise the query returns the existing realm
        entityManager.clear();

        User other = userService.find(user.getId());

        assertEquals(user, other);

    }


}
