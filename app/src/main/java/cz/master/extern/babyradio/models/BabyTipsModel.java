package cz.master.extern.babyradio.models;

import java.io.Serializable;

/**
 * Created by Yasir Iqbal on 7/3/2016.
 */
public class BabyTipsModel implements Serializable {
    String title, description;

    public BabyTipsModel( String title,String description) {
        this.description = description;
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
