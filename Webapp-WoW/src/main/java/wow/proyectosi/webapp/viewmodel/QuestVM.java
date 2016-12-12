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
 * Created by ndrs on 12/12/2016.
 */
public class QuestVM {
    private Quest currentQuest = null;

    public Quest getCurrentQuest() {
        return currentQuest;
    }

    public List<Quest> getQuests() {
        EntityManager em = getDesktopEntityManager();
        return em.createQuery("SELECT q FROM Quest q", Quest.class).getResultList();
    }

    @Command
    @NotifyChange("quests")
    public void delete(@BindingParam("q") Quest quest) {
        EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
        TransactionUtils.doTransaction(em, __ -> {
            em.remove(quest);
        });
    }

    @Command
    @NotifyChange("currentQuest")
    public void newQuest() {
        this.currentQuest = new Quest();
    }

    @Command
    @NotifyChange({ "quests", "currentQuest" })
    public void save() {
        EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
        TransactionUtils.doTransaction(em, __ -> {
            em.persist(this.currentQuest);
        });
        this.currentQuest = null;
    }

    @Command
    @NotifyChange("currentQuest")
    public void cancel() {
        this.currentQuest = null;
    }

    @Command
    @NotifyChange("currentQuest")
    public void edit(@BindingParam("q") Quest quest) {
        this.currentQuest = quest;
    }
}
