package fr.uga.l3miage.library.data.repo;

import fr.uga.l3miage.library.data.domain.Book;
import fr.uga.l3miage.library.data.domain.Borrow;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Repository
public class BorrowRepository implements CRUDRepository<String, Borrow> {

    private final EntityManager entityManager;

    @Autowired
    public BorrowRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Borrow save(Borrow entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Borrow get(String id) {
        return entityManager.find(Borrow.class, id);
    }

    @Override
    public void delete(Borrow entity) {
        entityManager.remove(entity);
    }

    @Override
    public List<Borrow> all() {
        return entityManager.createQuery("from Borrow", Borrow.class).getResultList();
    }

    /**
     * Trouver des emprunts en cours pour un emprunteur donné
     *
     * @param userId l'id de l'emprunteur
     * @return la liste des emprunts en cours
     */
    public List<Borrow> findInProgressByUser(long userId) {
    // public List<Borrow> findInProgressByUser(String userId) {
        return entityManager.createQuery("select b from Borrow b join b.borrower u where u.id = :userId and b.finished = false", Borrow.class).setParameter("userId", userId).getResultList();
        // return entityManager.createQuery("select b from Borrow b join b.borrower", Borrow.class).getResultList();
    }

    /**
     * Compte le nombre total de livres emprunté par un utilisateur.
     *
     * @param userId l'id de l'emprunteur
     * @return le nombre de livre
     */
    public int countBorrowedBooksByUser(long userId) {
    // public int countBorrowedBooksByUser(String userId) {
        List<Book> res = entityManager.createQuery("select bk from Borrow b join b.books bk where b.borrower.id = :userId", Book.class).setParameter("userId", userId).getResultList();
        return res.size()-1;
    }

    /**
     * Compte le nombre total de livres non rendu par un utilisateur.
     *
     * @param userId l'id de l'emprunteur
     * @return le nombre de livre
     */
    public int countCurrentBorrowedBooksByUser(long userId) {
    // public int countCurrentBorrowedBooksByUser(String userId) {
        List<Book> res = entityManager.createQuery("select bk from Borrow b join b.books bk where b.borrower.id = :userId and b.finished = false", Book.class).setParameter("userId", userId).getResultList();
        return res.size()-1;
    }

    /**
     * Recherche tous les emprunt en retard trié
     *
     * @return la liste des emprunt en retard
     */
    public List<Borrow> foundAllLateBorrow() {
        Date dateAct = Date.from((LocalDateTime.now()).atZone(ZoneId.systemDefault()).toInstant());
        return entityManager.createQuery("select b from Borrow b where b.requestedReturn < :dateAct and b.finished = false", Borrow.class).setParameter("dateAct", dateAct).getResultList();
    }

    /**
     * Calcul les emprunts qui seront en retard entre maintenant et x jours.
     *
     * @param days le nombre de jour avant que l'emprunt soit en retard
     * @return les emprunt qui sont bientôt en retard
     */
    public List<Borrow> findAllBorrowThatWillLateWithin(int days) {
        Date dateDeb = Date.from((LocalDateTime.now()).atZone(ZoneId.systemDefault()).toInstant());
        Date dateFin = Date.from((LocalDateTime.now().plusDays(days)).atZone(ZoneId.systemDefault()).toInstant());
        return entityManager.createQuery("select b from Borrow b where b.requestedReturn between :dateDeb and :dateFin and b.finished = false", Borrow.class).setParameter("dateDeb", dateDeb).setParameter("dateFin", dateFin).getResultList();
    }

}
