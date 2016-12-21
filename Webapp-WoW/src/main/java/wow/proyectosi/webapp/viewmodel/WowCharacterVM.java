package wow.proyectosi.webapp.viewmodel;

import entities.wow.proyectosi.*;
import entities.wow.proyectosi.WowCharacter;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import wow.proyectosi.TransactionUtils;
import wow.proyectosi.webapp.util.DesktopEntityManagerManager;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import static wow.proyectosi.webapp.util.DesktopEntityManagerManager.getDesktopEntityManager;

/**
 * ViewModel Class for WowCharacter.
 *
 * @author Andrés Vieira Vázquez
 * @version 1.0
 */
public class WowCharacterVM {
    private WowCharacter currentWowCharacter = null;

    /**
     * Return the current WowCharacter in the desktop.
     *
     * @return the Current WowCharacter.
     */
    public WowCharacter getCurrentWowCharacter() {
        return currentWowCharacter;
    }

    /**
     * Return a List containing all the WowCharacter rows in the database.
     *
     * @return List with all the WowCharacter.
     */
    public List<WowCharacter> getWowCharacters() {
        EntityManager em = getDesktopEntityManager();
        return em.createQuery("SELECT wc FROM WowCharacter wc", WowCharacter.class).getResultList();
    }

    /**
     * Return a List containing all the Party rows in the database.
     *
     * @return List with all the Parties.
     */
    public List<Party> getParties() {
        EntityManager em = getDesktopEntityManager();
        return em.createQuery("SELECT p FROM Party p", Party.class).getResultList();
    }

    /**
     * Return a List containing all the Quest rows in the database.
     *
     * @return List with all the Quests.
     */
    public List<Quest> getQuests() {
        EntityManager em = getDesktopEntityManager();
        return em.createQuery("SELECT q FROM Quest q", Quest.class).getResultList();
    }

    /**
     * Instantiate the currentWowCharacter to an empty WowCharacter.
     */
    @Command
    @NotifyChange("currentWowCharacter")
    public void newWowCharacter() {
        this.currentWowCharacter = new WowCharacter();
    }

    /**
     * Persists or updates the currentWowCharacter WowCharacter in the database.
     */
    @Command
    @NotifyChange({"wowCharacters", "currentWowCharacter"})
    public void save() {
        EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
        TransactionUtils.doTransaction(em, __ -> {
            em.persist(this.currentWowCharacter);
        });
        this.currentWowCharacter = null;
    }

    /**
     * Deletes a WowCharacter from the database.
     *
     * @param wowcharacter WowCharacter to be deleted.
     */
    @Command
    @NotifyChange("wowCharacters")
    public void delete(@BindingParam("wc") WowCharacter wowCharacter) {
        Set<Quest> copyQ = new HashSet<>(wowCharacter.getQuests());
        for (Quest q : copyQ
                ) {
            if (q.getWowcharacters().contains(wowCharacter)) {
                List<WowCharacter> copywc = new ArrayList<>(q.getWowcharacters());
                copywc.remove(wowCharacter);
                q.setWowcharacters(copywc);
            }
        }

        Set<WowCharacter> copyW = new HashSet<>(wowCharacter.getParty().getWowCharacters());
        copyW.remove(wowCharacter);
        wowCharacter.setParty(null);

        EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
        TransactionUtils.doTransaction(em, __ -> {
            em.remove(wowCharacter);
        });
    }

    /**
     * Set the currentWowCharacter WowCharacter to null.
     */
    @Command
    @NotifyChange("currentWowCharacter")
    public void cancel() {
        this.currentWowCharacter = null;
    }

    /**
     * Set the currentWowCharacter WowCharacter to the selected WowCharacter for edition.
     *
     * @param wowcharacter WowCharacter to be edited.
     */
    @Command
    @NotifyChange("currentWowCharacter")
    public void edit(@BindingParam("wc") WowCharacter wowcharacter) {
        this.currentWowCharacter = wowcharacter;
    }
}
