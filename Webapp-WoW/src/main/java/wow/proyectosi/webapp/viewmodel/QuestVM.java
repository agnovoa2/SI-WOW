package wow.proyectosi.webapp.viewmodel;

import entities.wow.proyectosi.Quest;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import wow.proyectosi.TransactionUtils;
import wow.proyectosi.webapp.util.DesktopEntityManagerManager;

import javax.persistence.EntityManager;
import java.util.List;

import static wow.proyectosi.webapp.util.DesktopEntityManagerManager.getDesktopEntityManager;

/**
 * ViewModel Class for Quest.
 *
 * @author Andrés Vieira Vázquez
 * @version 1.0
 */
public class QuestVM {
    private Quest currentQuest = null;

    /**
     * Return the current Quest in the desktop.
     *
     * @return the Current Quest.
     */
    public Quest getCurrentQuest() {
        return currentQuest;
    }

    /**
     * Return a List containing all the Quest rows in the database.
     *
     * @return List with all the Quest.
     */
    public List<Quest> getQuests() {
        EntityManager em = getDesktopEntityManager();
        return em.createQuery("SELECT q FROM Quest q", Quest.class).getResultList();
    }

    /**
     * Deletes a Quest from the database.
     *
     * @param quest Quest to be deleted.
     */
    @Command
    @NotifyChange("quests")
    public void delete(@BindingParam("q") Quest quest) {
        EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
        TransactionUtils.doTransaction(em, __ -> {
            em.remove(quest);
        });
    }

    /**
     * Instantiate the currentQuest to an empty Quest.
     */
    @Command
    @NotifyChange("currentQuest")
    public void newQuest() {
        this.currentQuest = new Quest();
    }

    /**
     * Persists or updates the currentQuest Quest in the database.
     */
    @Command
    @NotifyChange({"quests", "currentQuest"})
    public void save() {
        EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
        TransactionUtils.doTransaction(em, __ -> {
            em.persist(this.currentQuest);
        });
        this.currentQuest = null;
    }

    /**
     * Set the currentQuest Quest to null.
     */
    @Command
    @NotifyChange("currentQuest")
    public void cancel() {
        this.currentQuest = null;
    }

    /**
     * Set the currentQuest Quest to the selected Quest for edition.
     *
     * @param quest Quest to be edited.
     */
    @Command
    @NotifyChange("currentQuest")
    public void edit(@BindingParam("q") Quest quest) {
        this.currentQuest = quest;
    }
}
