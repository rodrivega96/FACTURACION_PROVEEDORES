package com.vates.eng.ha.persistence;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.vates.eng.ha.persistence.domain.Realm;
import com.vates.eng.ha.persistence.service.RealmService;

/**
 * @author Gaston Napoli
 *
 */
@ContextConfiguration(classes = { com.vates.eng.ha.persistence.TestPersistenceConfig.class })
@ActiveProfiles("ha.db.test")
public class RealmServiceTest extends AbstractTransactionalTestNGSpringContextTests {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private RealmService realmService;

    @Test
    @Transactional
    public void testSaveAndFindRepository() throws Exception {

        Realm realm = new Realm();

        realm.setChangingPasswordEnabled(true);

        realm.setName("nombre");

        realm.setHandledBy("dummyHandler");

        realm.setDescription("Descripcion generica");

        entityManager.persist(realm);

        entityManager.flush();

        // Otherwise the query returns the existing realm
        entityManager.clear();

        Realm other = realmService.find(realm.getId());

        assertEquals(realm, other);

    }

}
