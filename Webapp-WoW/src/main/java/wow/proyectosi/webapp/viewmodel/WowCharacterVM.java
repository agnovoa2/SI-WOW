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
 * Created by ndrs on 12/12/2016.
 */
public class WowCharacterVM {
    private WowCharacter currentWowCharacter = null;

    public WowCharacter getCurrentWowCharacter() {
        return currentWowCharacter;
    }

    public List<WowCharacter> getWowCharacters() {
        EntityManager em = getDesktopEntityManager();
        return em.createQuery("SELECT wc FROM WowCharacter wc", WowCharacter.class).getResultList();
    }

    public List<Party> getParties() {
        EntityManager em = getDesktopEntityManager();
        return em.createQuery("SELECT p FROM Party p", Party.class).getResultList();
    }

    public List<Quest> getQuests() {
        EntityManager em = getDesktopEntityManager();
        return em.createQuery("SELECT q FROM Quest q", Quest.class).getResultList();
    }

    @Command
    @NotifyChange("currentWowCharacter")
    public void newWowCharacter() {
        this.currentWowCharacter = new WowCharacter();
    }

    @Command
    @NotifyChange({"wowCharacters", "currentWowCharacter"})
    public void save() {
        EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
        TransactionUtils.doTransaction(em, __ -> {
            em.persist(this.currentWowCharacter);
        });
        this.currentWowCharacter = null;
    }

    @Command
    @NotifyChange("wowCharacters")
    public void delete(@BindingParam("wc") WowCharacter wowCharacter) {
        Set<Quest> copyQ = new HashSet<>(wowCharacter.getQuests());
        for (Quest q: copyQ
             ) {
            if (q.getWowcharacters().contains(wowCharacter)){
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

    @Command
    @NotifyChange("currentWowCharacter")
    public void cancel() {
        this.currentWowCharacter = null;
    }

    @Command
    @NotifyChange("currentWowCharacter")
    public void edit(@BindingParam("wc") WowCharacter wowcharacter) {
        this.currentWowCharacter = wowcharacter;
    }
}
