
package it.mam.REST.controller.front;

import it.mam.REST.data.model.Episode;
import java.util.List;

/**
 *
 * @author Mirko
 */
public class Season {
    private int number;
    private List<Episode> episodeList;
    
    public Season(){
this.number = 0;
this.episodeList = null;
}
    public Season(int n, List<Episode> e){
        this();
        this.number = n;
        this.episodeList = e;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<Episode> getEpisodeList() {
        return episodeList;
    }

    public void setEpisodeList(List<Episode> episodeList) {
        this.episodeList = episodeList;
    }
    
}

