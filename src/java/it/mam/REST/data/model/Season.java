package it.mam.REST.data.model;

import java.util.List;

/**
 *
 * @author Mirko
 */
public class Season {

    private int number;
    private List<Episode> episodes;

    public Season() {
        this.number = 0;
        this.episodes = null;
    }

    public Season(int n, List<Episode> e) {
        this();
        this.number = n;
        this.episodes = e;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

}
